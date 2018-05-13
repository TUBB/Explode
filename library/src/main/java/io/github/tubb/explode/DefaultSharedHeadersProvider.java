package io.github.tubb.explode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

/**
 * SharedHeadersProvider default impl, add User-Agent header
 * Created by tubingbing on 18/3/30.
 */

class DefaultSharedHeadersProvider implements SharedHeadersProvider {
    @Nullable
    @Override
    public ArrayMap<String, String> provide(@NonNull Context context, @NonNull String url) {
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("User-Agent", String.format("explode_%s/Android", BuildConfig.VERSION_NAME));
        return headers;
    }
}
