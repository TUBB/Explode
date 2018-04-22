package io.github.tubb.explode;

import android.os.Looper;
import android.support.v4.util.ArrayMap;

/**
 * Check utils
 * Created by tubingbing on 2017/12/18.
 */

final class CheckUtils {
    private CheckUtils() {}

    static <T> T checkNotNull(T ref, String msg) {
        if (ref == null) {
            throw new NullPointerException(msg);
        }
        return ref;
    }

    static <T> boolean isNull(T ref) {
        return ref == null;
    }

    static void checkOnMainThread(String msg) {
        if (!(Thread.currentThread() == Looper.getMainLooper().getThread())) {
            throw new RuntimeException(msg);
        }
    }

    static boolean isEmpty(ArrayMap map) {
        return map == null || map.size() <= 0;
    }

    static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
}
