package io.github.tubb.explode;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Http thread compose transformer
 * Created by tubingbing on 18/4/8.
 */

class NetworkThreadTransformer<D> implements ObservableTransformer<D, D>{
    @Override
    public ObservableSource<D> apply(Observable<D> upstream) {
        return upstream
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui());
    }
}
