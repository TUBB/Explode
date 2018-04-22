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
    protected void configClientCookie(final String baseUrl) {
        super.configClientCookie(baseUrl);
    }

    @Override
    protected void configClientInterceptor(final String baseUrl) {
        super.configClientInterceptor(baseUrl);
    }

    @Override
    protected void configClientTimeout(final String baseUrl) {
        super.configClientTimeout(baseUrl);
    }
}
