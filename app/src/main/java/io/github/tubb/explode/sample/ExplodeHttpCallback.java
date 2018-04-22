package io.github.tubb.explode.sample;

import io.github.tubb.explode.HttpCallback;

/**
 * 全局统一的接口数据处理，根据不同的业务数据格式做相应的处理，但整体流程一致
 * Created by tubingbing on 18/4/2.
 */

public abstract class ExplodeHttpCallback<D> implements HttpCallback<HttpResult<D>, D> {

    @Override
    public void beforeOnSuccess(HttpResult<D> data) {
        if ("success".equals(data.code)) {
            onSuccess(data.data);
        } else { // http正常返回，但服务端业务处理异常
            beforeOnError(new ServerInternalException(data.msg));
        }
    }

    @Override
    public void beforeOnError(Throwable throwable) {
        if (!(throwable instanceof ServerInternalException)) {
            // 网络异常
            onError(new Exception("网络异常"));
        } else {
            // 业务异常
            onError(throwable);
        }
    }

    @Override
    public void onStart() {
        // 请求开始
    }

    @Override
    public void onFinish() {
        // 请求结束
    }

    public static class ServerInternalException extends Throwable {
        ServerInternalException(String msg) {
            super(msg);
        }
    }
}
