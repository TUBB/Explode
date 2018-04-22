package io.github.tubb.explode;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import static io.github.tubb.explode.CheckUtils.checkNotNull;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * Config explode instance
 * Created by tubingbing on 18/3/30.
 */

public final class ExplodeConfig {
    private Context context;
    private SharedHeadersProvider sharedHeadersProvider;
    private CookieStrategy cookieStrategy;
    private boolean debug = false;
    private long netReadTimeout;
    private long netWriteTimeout;
    private long netConnectTimeout;
    private OkHttpCacheProxy okHttpCacheProxy;

    private ExplodeConfig(Builder builder) {
        this.context = builder.context;
        if (!isNull(builder.sharedHeadersProvider)) {
            this.sharedHeadersProvider = builder.sharedHeadersProvider;
        } else {
            this.sharedHeadersProvider = new DefaultSharedHeadersProvider();
        }
        this.cookieStrategy = builder.cookieStrategy;
        this.debug = builder.debug;
        this.netReadTimeout = builder.netReadTimeout;
        this.netWriteTimeout = builder.netWriteTimeout;
        this.netConnectTimeout = builder.netConnectTimeout;
        this.okHttpCacheProxy = builder.okHttpCacheProxy;
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    @NonNull
    public SharedHeadersProvider getSharedHeadersProvider() {
        return sharedHeadersProvider;
    }

    @Nullable
    public CookieStrategy getCookieStrategy() {
        return cookieStrategy;
    }

    public boolean isDebug() {
        return debug;
    }

    public long getNetReadTimeout() {
        return netReadTimeout;
    }

    public long getNetWriteTimeout() {
        return netWriteTimeout;
    }

    public long getNetConnectTimeout() {
        return netConnectTimeout;
    }

    @Nullable
    public OkHttpCacheProxy getOkHttpCacheProxy() {
        return okHttpCacheProxy;
    }

    public final static class Builder {
        private static final int READ_TIMEOUT = 60 * 1000;
        private static final int WRIT_TIMEOUT = 60 * 1000;
        private static final int CONNECT_TIMEOUT = 60 * 1000;
        private Context context;
        private SharedHeadersProvider sharedHeadersProvider;
        private CookieStrategy cookieStrategy;
        private boolean debug = false;
        private long netReadTimeout;
        private long netWriteTimeout;
        private long netConnectTimeout;
        private OkHttpCacheProxy okHttpCacheProxy;

        public Builder(@NonNull Application application) {
            this.context = checkNotNull(application, "application == null");
        }

        public Builder sharedHeadersProvider(@NonNull SharedHeadersProvider sharedHeadersProvider) {
            this.sharedHeadersProvider = checkNotNull(sharedHeadersProvider, "sharedHeadersProvider == null");
            return this;
        }

        public Builder cookieStrategy(@NonNull CookieStrategy cookieStrategy) {
            this.cookieStrategy = checkNotNull(cookieStrategy, "cookieStrategy == null");
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder netReadTimeout(long readTimeout) {
            this.netReadTimeout = readTimeout;
            return this;
        }

        public Builder netWriteTimeout(long writeTimeout) {
            this.netWriteTimeout = writeTimeout;
            return this;
        }

        public Builder netConnectTimeout(long connectTimeout) {
            this.netConnectTimeout = connectTimeout;
            return this;
        }

        public Builder okHttpCache(File cacheDir, int cacheSize) {
            checkNotNull(cacheDir, "cacheDir == null");
            if (cacheSize <= 0) {
                throw new IllegalArgumentException("cacheSize <= 0");
            }
            this.okHttpCacheProxy = new OkHttpCacheProxy(cacheDir, cacheSize);
            return this;
        }

        public ExplodeConfig build() {
            if (this.netReadTimeout <= 0) {
                this.netReadTimeout = READ_TIMEOUT;
            }
            if (this.netWriteTimeout <= 0) {
                this.netWriteTimeout = WRIT_TIMEOUT;
            }
            if (this.netConnectTimeout <= 0) {
                this.netConnectTimeout = CONNECT_TIMEOUT;
            }
            return new ExplodeConfig(this);
        }
    }
}
