package io.github.tubb.explode;

import io.reactivex.disposables.Disposable;

import static io.github.tubb.explode.CheckUtils.checkNotNull;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * Cancel http request(Observable) for business model
 * Created by tubingbing on 18/4/2.
 */

final class HttpCanceler<W, D> {
    private Disposable disposable;
    private HttpCallback<W, D> callback;

    HttpCanceler(Disposable disposable, HttpCallback<W, D> callback) {
        this.disposable = checkNotNull(disposable, "disposable == null");
        this.callback = checkNotNull(callback, "callback == null");
    }

    void cancel() {
        if (!disposable.isDisposed())
            disposable.dispose();
        this.callback = null;
    }

    boolean isCanceled() {
        return disposable.isDisposed() && isNull(callback);
    }
}
