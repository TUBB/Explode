package io.github.tubb.explode;

import android.support.v4.util.ArrayMap;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * OkHttp memory cookie store strategy
 * Created by tubingbing on 18/3/30.
 */

public final class MemoryCookieStrategy implements CookieStrategy {
    private final ArrayMap<String, List<Cookie>> cookieStore = new ArrayMap<>();

    @Override
    public void save(HttpUrl httpUrl, List<Cookie> list) {
        cookieStore.put(httpUrl.host(), list);
    }

    @Override
    public List<Cookie> load(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : Collections.<Cookie>emptyList();
    }
}
