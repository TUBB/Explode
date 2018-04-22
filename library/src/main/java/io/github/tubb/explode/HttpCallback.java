package io.github.tubb.explode;

/**
 *
 * Created by tubingbing on 18/4/2.
 */

public interface HttpCallback<W, D> {
    void beforeOnSuccess(W data);
    void beforeOnError(Throwable throwable);
    void onStart();
    void onSuccess(D data);
    void onError(Throwable throwable);
    void onFinish();
}
