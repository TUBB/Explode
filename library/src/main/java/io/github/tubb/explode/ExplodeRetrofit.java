package io.github.tubb.explode;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static io.github.tubb.explode.CheckUtils.checkNotNull;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * Retrofit(2.4.0) proxy
 * Note: only newBuilder() method not proxy
 * Created by tubingbing on 18/3/30.
 */

public final class ExplodeRetrofit {
    private Retrofit retrofit;

    private ExplodeRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    public okhttp3.Call.Factory callFactory() {
        return retrofit.callFactory();
    }

    public HttpUrl baseUrl() {
        return retrofit.baseUrl();
    }

    public List<CallAdapter.Factory> callAdapterFactories() {
        return retrofit.callAdapterFactories();
    }

    public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
        return retrofit.callAdapter(returnType, annotations);
    }

    public CallAdapter<?, ?> nextCallAdapter(CallAdapter.Factory skipPast, Type returnType,
                                             Annotation[] annotations) {
        return retrofit.nextCallAdapter(skipPast, returnType, annotations);
    }

    public List<Converter.Factory> converterFactories() {
        return retrofit.converterFactories();
    }

    public <T> Converter<T, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations) {
        return retrofit.requestBodyConverter(type, parameterAnnotations, methodAnnotations);
    }

    public <T> Converter<T, RequestBody> nextRequestBodyConverter(
            Converter.Factory skipPast, Type type, Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations) {
        return retrofit.nextRequestBodyConverter(skipPast, type, parameterAnnotations, methodAnnotations);
    }

    public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotations) {
        return retrofit.responseBodyConverter(type, annotations);
    }

    public <T> Converter<ResponseBody, T> nextResponseBodyConverter(
            Converter.Factory skipPast, Type type, Annotation[] annotations) {
        return retrofit.nextResponseBodyConverter(skipPast, type, annotations);
    }

    public <T> Converter<T, String> stringConverter(Type type, Annotation[] annotations) {
        return retrofit.stringConverter(type, annotations);
    }

    public @Nullable Executor callbackExecutor() {
        return retrofit.callbackExecutor();
    }

    public static final class Builder {
        private OkHttpClient client;
        private okhttp3.Call.Factory callFactory;
        private HttpUrl httpUrl;
        private final List<Converter.Factory> converterFactories = new ArrayList<>();
        private final List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private Executor callbackExecutor;
        private boolean validateEagerly;

        public Builder() {}

        public Builder client(OkHttpClient client) {
            this.client = checkNotNull(client, "client == null");
            return this;
        }

        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.callFactory = checkNotNull(factory, "factory == null");
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            checkNotNull(baseUrl, "baseUrl == null");
            httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl == null) {
                throw new IllegalArgumentException("Illegal URL: " + baseUrl);
            }
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            converterFactories.add(checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            callAdapterFactories.add(checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = checkNotNull(executor, "executor == null");
            return this;
        }

        public Builder validateEagerly(boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }

        public ExplodeRetrofit build() {
            if (httpUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            Retrofit.Builder builder = new Retrofit.Builder();
            if (!isNull(client)) builder.client(client);
            if (!isNull(callFactory)) builder.callFactory(callFactory);
            if (!isNull(httpUrl)) builder.baseUrl(httpUrl);
            if (!isNull(callbackExecutor)) builder.callbackExecutor(callbackExecutor);
            builder.validateEagerly(validateEagerly);
            for (Converter.Factory converter : converterFactories) {
                builder.addConverterFactory(converter);
            }
            for (CallAdapter.Factory adapter : callAdapterFactories) {
                builder.addCallAdapterFactory(adapter);
            }
            return new ExplodeRetrofit(builder.build());
        }
    }
}
