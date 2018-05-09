package io.github.tubb.explode;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import static io.github.tubb.explode.CheckUtils.isNull;
import static io.github.tubb.explode.HttpLoggingInterceptor.Level.BODY;

/**
 * Explode retrofit generator
 * Created by tubingbing on 18/3/30.
 */

public abstract class ExplodeRetrofitGenerator {
    protected ExplodeConfig explodeConfig;
    protected OkHttpClient.Builder clientBuilder;
    protected ExplodeRetrofit.Builder explodeRetrofitBuilder;

    private OkHttpClient okHttpClient;

    protected ExplodeRetrofitGenerator() {
        explodeConfig = Explode.instance().getConfig();
        clientBuilder = new OkHttpClient.Builder();
        explodeRetrofitBuilder = new ExplodeRetrofit.Builder();
    }

    private void configClientTimeout() {
        clientBuilder.readTimeout(explodeConfig.getNetReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(explodeConfig.getNetWriteTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(explodeConfig.getNetConnectTimeout(), TimeUnit.MILLISECONDS);
    }

    private void configClientInterceptor() {
        for (Interceptor interceptor :
                explodeConfig.getOkHttpInterceptors()) {
            clientBuilder.addInterceptor(interceptor);
        }
        for (Interceptor interceptor :
                explodeConfig.getOkHttpNetworkInterceptors()) {
            clientBuilder.addNetworkInterceptor(interceptor);
        }
        if (explodeConfig.isDebug()) {
            clientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
        }
    }

    private void configClientCookie() {
        final CookieStrategy cookieStrategy = explodeConfig.getCookieStrategy();
        if (!isNull(cookieStrategy)) {
            clientBuilder.cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                    cookieStrategy.save(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                    return cookieStrategy.load(url);
                }
            });
        }
    }

    private void configOkHttpCache() {
        OkHttpCacheProxy okHttpCacheProxy = Explode.instance().getConfig().getOkHttpCacheProxy();
        if (!isNull(okHttpCacheProxy))
            clientBuilder.cache(okHttpCacheProxy.cache);
    }

    private void configRetrofit(final String baseUrl) {
        explodeRetrofitBuilder.baseUrl(baseUrl)
                .client(buildClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @NonNull protected ExplodeRetrofit get(@NonNull String baseUrl) {
        configOkHttpClient();
        configRetrofit(baseUrl);
        return buildExplodeRetrofit();
    }

    private void configOkHttpClient() {
        if (isNull(okHttpClient)) {
            configClientTimeout();
            configClientInterceptor();
            configClientCookie();
            configOkHttpCache();
        }
    }

    private ExplodeRetrofit buildExplodeRetrofit() {
        return explodeRetrofitBuilder.build();
    }

    private OkHttpClient buildClient() {
        return isNull(okHttpClient) ? okHttpClient = clientBuilder.build() : okHttpClient;
    }

    /**
     * OkHttp不和Retrofit一起使用时，应调用此方法获得OkHttpClient
     * @return OkHttpClient
     */
    OkHttpClient getOkHttpClient() {
        configOkHttpClient();
        return buildClient();
    }

    @NonNull protected String id() {
        return "default";
    }
}
