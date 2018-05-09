package io.github.tubb.explode.sample;

import android.support.annotation.NonNull;

import io.github.tubb.explode.ExplodeRetrofit;
import io.github.tubb.explode.ExplodeRetrofitGenerator;

/**
 * Created by tubingbing on 18/4/2.
 */

public class TestExplodeRetrofitGenerator extends ExplodeRetrofitGenerator {

    TestExplodeRetrofitGenerator() {
        super();
    }

    @NonNull
    @Override
    protected String id() {
        return "test";
    }
}
