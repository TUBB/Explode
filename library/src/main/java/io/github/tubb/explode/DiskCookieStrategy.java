package io.github.tubb.explode;

import android.app.Application;
import android.content.Context;
import android.support.annotation.WorkerThread;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static io.github.tubb.explode.CheckUtils.checkNotNull;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * OkHttp disk cookie store strategy
 * Created by tubingbing on 18/4/3.
 */

public class DiskCookieStrategy implements CookieStrategy {
    private Context mContext;
    private PersistentCookieStore mCookieStore;

    public DiskCookieStrategy(Application application) {
        mContext = checkNotNull(application, "application == null");
    }

    @WorkerThread
    @Override
    public synchronized void save(HttpUrl httpUrl, List<Cookie> list) {
        init();
        mCookieStore.add(httpUrl, list);
    }

    @WorkerThread
    @Override
    public List<Cookie> load(HttpUrl httpUrl) {
        synchronized (this) {
            init();
        }
        return mCookieStore.get(httpUrl);
    }

    private void init() {
        if (isNull(mCookieStore))
            mCookieStore = new PersistentCookieStore(mContext);
    }
}
