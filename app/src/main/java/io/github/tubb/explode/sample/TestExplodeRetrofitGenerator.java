package io.github.tubb.explode.sample;

import android.support.annotation.NonNull;

import io.github.tubb.explode.ExplodeRetrofit;
import io.github.tubb.explode.ExplodeRetrofitGenerator;

/**
 * Created by tubingbing on 18/4/2.
 */

public class TestExplodeRetrofitGenerator extends ExplodeRetrofitGenerator {
    @NonNull
    @Override
    protected ExplodeRetrofit get(@NonNull String baseUrl) {
        return super.get(baseUrl);
    }

    @Override
    protected void configClientCookie() {
        super.configClientCookie();
    }

    @Override
    protected void configClientInterceptor() {
        super.configClientInterceptor();
    }

    @Override
    protected void configClientTimeout() {
        super.configClientTimeout();
    }
}
