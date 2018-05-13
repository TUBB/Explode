package io.github.tubb.explode;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.io.IOException;
import java.util.Map.Entry;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static io.github.tubb.explode.CheckUtils.isEmpty;

/**
 * Shared http headers for okhttp3
 * Created by tubingbing on 18/3/30.
 */

final class SharedHeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originRequest = chain.request();
        String url = originRequest.url().toString();
        ExplodeConfig config = Explode.instance().getConfig();
        ArrayMap<String, String> sharedHeaders =
                config.getSharedHeadersProvider()
                .provide(config.getContext(), url);
        Request.Builder builder = originRequest.newBuilder();
        if (!isEmpty(sharedHeaders)) {
            for (Entry<String, String> header : sharedHeaders.entrySet()){
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
        // 添加默认的User-Agent头
        if (isEmpty(sharedHeaders) || !sharedHeaders.containsKey("User-Agent")) {
            builder.addHeader("User-Agent", String.format("explode_%s/Android", BuildConfig.VERSION_NAME));
        }
        return chain.proceed(builder.build());
    }
}
