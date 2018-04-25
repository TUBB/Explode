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

final class SchedulerProvider {
    private SchedulerProvider() {}

    static Scheduler computation() {
        return Schedulers.computation();
    }

    static Scheduler io() {
        return Schedulers.io();
    }

    static Scheduler trampoline() {
        return Schedulers.trampoline();
    }

    static Scheduler newThread() {
        return Schedulers.newThread();
    }

    static Scheduler single() {
        return Schedulers.single();
    }

    static Scheduler from(Executor executor) {
        return Schedulers.from(executor);
    }

    static void shutdown() {
        Schedulers.shutdown();
    }

    static void start() {
        Schedulers.start();
    }

    static Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    static Scheduler from(Looper looper) {
        return AndroidSchedulers.from(looper);
    }
}
