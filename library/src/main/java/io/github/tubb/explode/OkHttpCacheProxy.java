package io.github.tubb.explode;

import java.io.File;

import okhttp3.Cache;

/**
 * OkHttp Cache proxy
 * Created by tubingbing on 18/4/3.
 */

class OkHttpCacheProxy {
    Cache cache;
    OkHttpCacheProxy(File cacheDir, int cacheSize) {
        this.cache = new Cache(cacheDir, cacheSize);
    }
}
