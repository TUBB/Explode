package io.github.tubb.explode.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import io.github.tubb.explode.BuildConfig;
import io.github.tubb.explode.SharedHeadersProvider;

/**
 * SharedHeadersProvider default impl, add User-Agent header
 * Created by tubingbing on 18/3/30.
 */

class TestSharedHeadersProvider implements SharedHeadersProvider {
    @Nullable
    @Override
    public ArrayMap<String, String> provide(@NonNull String url) {
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("User-Agent", String.format("explode_%s/Android", BuildConfig.VERSION_NAME));
        headers.put("Token", "12345678998");
        return headers;
    }
}
