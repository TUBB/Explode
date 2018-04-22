package io.github.tubb.explode;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Cookie disk store
 * Created by tubingbing on 18/4/3.
 */

class PersistentCookieStore {
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final HashMap<String, ConcurrentHashMap<String, Cookie>> mCookies;
    private final SharedPreferences mCookiePrefs;
    private final SDHttpCookie mSDHttpCookie;

    /**
     * Construct a persistent cookie store.
     * @param context Context to attach cookie store to
     */
    @WorkerThread
    PersistentCookieStore(Context context) {
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        mSDHttpCookie = new JSONSDHttpCookie();
        mCookies = new HashMap<>();
        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (null != entry.getValue() && !((String) entry.getValue()).startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = mCookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = desCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!mCookies.containsKey(entry.getKey()))
                                mCookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                            mCookies.get(entry.getKey()).put(name, decodedCookie);
                        }
                    }
                }
            }
        }
    }

    private Cookie desCookie(String cookieStr) {
        return mSDHttpCookie.des(cookieStr);
    }

    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + cookie.domain();
    }

    public void add(HttpUrl httpUrl, List<Cookie> cookies) {
        if (null != cookies && cookies.size() > 0) {
            for (Cookie item : cookies) {
                add(item);
            }
        }
    }

    private void add(Cookie cookie) {
        // save cookie into local store, or remove if expired
        if (!mCookies.containsKey(cookie.domain()))
            mCookies.put(cookie.domain(), new ConcurrentHashMap<String, Cookie>());
        if (cookie.expiresAt() > System.currentTimeMillis()) {
            mCookies.get(cookie.domain()).put(cookie.name(), cookie);
        } else {
            if (mCookies.containsKey(cookie.domain()))
                mCookies.get(cookie.domain()).remove(cookie.domain());
        }
        // save cookie into persistent store
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.putString(cookie.domain(), TextUtils.join(",", mCookies.get(cookie.domain()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + cookie.name(), serCookie(cookie));
        prefsWriter.apply();
    }

    private String serCookie(Cookie cookie) {
        return mSDHttpCookie.ser(cookie);
    }

    public List<Cookie> get(HttpUrl uri) {
        ArrayList<Cookie> ret = new ArrayList<>();
        for (String key : mCookies.keySet()) {
            if (uri.host().contains(key)) {
                ret.addAll(mCookies.get(key).values());
            }
        }
        return ret;
    }
}
