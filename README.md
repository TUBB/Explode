# Explode


网络请求框架，提供统一可配置的网络请求接口

* 可配置Retrofit生成器，基于baseUrl和生成器id缓存Retrofit对象
* 基于Retrofit注解来构建OkHttp3 Request
* 基于OkHttp3拦截器，支持配置公共请求头
* Cookie缓存策略可定制化
* 支持配置OkHttp3 Http响应缓存
* 请求可取消
* 支持`RxJava.Observable`、`Retrofit.Call`和`OkHttp3.Request`作为执行体

# Download

```groovy
implementation 'io.github.tubb:explode:0.0.1'
```

# Usage

## 初始化

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Explode.init(this);
    }
}
```

## 定义Retrofit Service
```java
public interface UserService {
    @GET("/users")
    Observable<HttpResult<ArrayList<User>>> users();
    @GET("/users")
    Call<HttpResult<ArrayList<User>>> listUsers();
}
```

## 定义HttpResult响应实体
可以根据自身APP统一的响应数据格式来定义HttpResult，比如：
```json
{
  "code":"success",
  "msg":"",
  "data":[
    {
      "username":"小宋佳"
    },
    {
      "username":"闯关东"
    }
  ]
}
```

```java
public final class HttpResult<D> {
    public String code;
    public String msg;
    public D data;
}
```

## 实现HttpCallback接口

由于各种APP的业务不尽相同，针对响应数据的处理要基于具体的业务，所以框架没有提供具体的实现，只是定义了业务方要实现的接口。要根据自己APP的特点来实现HttpCallback接口，下面给出一个简单的实现
```java
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
```

## 执行具体的请求

Explode接受三种对象作为执行体，`RxJava.Observable`、`Retrofit.Call`和`OkHttp3.Request`，比较常用的可能是`RxJava.Observable`，示例如下：
```java
private void executeRxJava() {
    fetchUsersTaskId = Explode.instance().execute(userService.users(), new ExplodeHttpCallback<ArrayList<User>>() {
        @Override
        public void onSuccess(ArrayList<User> data) {
            String usersStr = data.toString();
            tvMsg.setText(usersStr);
            showErrorToast(usersStr);
        }

        @Override
        public void onError(Throwable throwable) {
            String error = throwable.getMessage();
            tvMsg.setText(error);
            showErrorToast(error);
        }
    });
}
```

## 取消请求
离开当前Activity时有必要取消还未完成的网络请求，避免Activity实例的泄漏
```java
@Override
protected void onDestroy() {
	super.onDestroy();
	Explode.instance().cancel(fetchUsersTaskId);
}
```

# 自定义

在初始化Explode时，可以提供ExplodeConfig对象，来配置一些自定义实现。现主要包含：
- 调试期log
- Cookie的缓存策略
- 通用的请求Header
- 应用服务端设置的Http缓存头
- 网络的读、写和连接超时时间
```java
Explode.init(new ExplodeConfig.Builder(this)
        // 调试阶段
        .debug(true)
        // 对Cookie应用磁盘缓存策略
        .cookieStrategy(new DiskCookieStrategy(this))
        // 只会对get请求应用服务器端设置的缓存头，5M大小
        .okHttpCache(getExternalCacheDir(), 5 * 1024 * 1024)
        .sharedHeadersProvider(new TestSharedHeadersProvider())
        .netConnectTimeout(60 * 1000)
        .netReadTimeout(60 * 1000)
        .netWriteTimeout(60 * 1000)
        .build());
```
