package io.github.tubb.explode;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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

    protected void configClientTimeout() {
        clientBuilder.readTimeout(explodeConfig.getNetReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(explodeConfig.getNetWriteTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(explodeConfig.getNetConnectTimeout(), TimeUnit.MILLISECONDS);
    }

    protected void configClientInterceptor() {
        clientBuilder.addInterceptor(new SharedHeadersInterceptor());
        if (explodeConfig.isDebug()) {
            clientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
        }
    }

    protected void configClientCookie() {
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

    protected void configOkHttpCache() {
        OkHttpCacheProxy okHttpCacheProxy = Explode.instance().getConfig().getOkHttpCacheProxy();
        if (!isNull(okHttpCacheProxy))
            clientBuilder.cache(okHttpCacheProxy.cache);
    }

    protected void configRetrofit(final String baseUrl) {
        explodeRetrofitBuilder.baseUrl(baseUrl)
                .client(buildClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @NonNull protected ExplodeRetrofit get(@NonNull String baseUrl) {
        configClientTimeout();
        configClientInterceptor();
        configClientCookie();
        configOkHttpCache();
        configRetrofit(baseUrl);
        return buildExplodeRetrofit();
    }

    protected ExplodeRetrofit buildExplodeRetrofit() {
        return explodeRetrofitBuilder.build();
    }

    protected OkHttpClient buildClient() {
        return clientBuilder.build();
    }

    /**
     * OkHttp不和Retrofit一起使用时，应调用此方法获得OkHttpClient
     * @return OkHttpClient
     */
    OkHttpClient getOkHttpClient() {
        if (isNull(okHttpClient)) {
            configClientTimeout();
            configClientInterceptor();
            configClientCookie();
            configOkHttpCache();
            okHttpClient = buildClient();
        }
        return okHttpClient;
    }

    @NonNull protected String id() {
        return "default";
    }
}
