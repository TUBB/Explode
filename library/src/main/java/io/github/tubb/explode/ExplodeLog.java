package io.github.tubb.explode;

import android.util.Log;

/**
 * Internal log utils
 * Created by tubingbing on 18/3/30.
 */

final class ExplodeLog {
    private static final String TAG = "Explode";
    static boolean DEBUG = false;

    static void d(String msg) {
        Log.d(TAG, msg);
    }

    static void w(String msg) {
        Log.w(TAG, msg);
    }

    static void w(Throwable thr) {
        Log.w(TAG, thr);
    }

    static void e(String msg) {
        Log.e(TAG, msg);
    }

    static void e(String msg, Throwable thr) {
        Log.e(TAG, msg, thr);
    }

    static void setDebug(boolean enable) {
        DEBUG = enable;
    }
}
