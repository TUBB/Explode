package io.github.tubb.explode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

/**
 * Http shared headers provider
 * Created by tubingbing on 18/3/30.
 */

public interface SharedHeadersProvider {
    @Nullable
    ArrayMap<String, String> provide(@NonNull Context context, @NonNull String url);
}
