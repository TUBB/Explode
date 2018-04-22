package io.github.tubb.explode;

import android.os.Looper;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava Scheduler provider, proxy of Schedulers and AndroidSchedulers
 * Created by tubingbing on 2017/12/18.
 */

public final class SchedulerProvider {
    private SchedulerProvider() {}

    public static Scheduler computation() {
        return Schedulers.computation();
    }

    public static Scheduler io() {
        return Schedulers.io();
    }

    public static Scheduler trampoline() {
        return Schedulers.trampoline();
    }

    public static Scheduler newThread() {
        return Schedulers.newThread();
    }

    public static Scheduler single() {
        return Schedulers.single();
    }

    public static Scheduler from(Executor executor) {
        return Schedulers.from(executor);
    }

    public static void shutdown() {
        Schedulers.shutdown();
    }

    public static void start() {
        Schedulers.start();
    }

    public static Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler from(Looper looper) {
        return AndroidSchedulers.from(looper);
    }
}
