package io.github.tubb.explode.sample;

import android.app.Application;

import io.github.tubb.explode.DiskCookieStrategy;
import io.github.tubb.explode.Explode;
import io.github.tubb.explode.ExplodeConfig;

/**
 * Created by tubingbing on 18/3/30.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Explode.init(new ExplodeConfig.Builder(this)
                // 调试阶段
                .debug(true)
                // 为Retrofit配置统一的base url
                .baseUrl("http://10.0.2.177:3000")
                // 对Cookie应用磁盘缓存策略
                .cookieStrategy(new DiskCookieStrategy(this))
                // 对Cookie应用memory缓存策略
                //.cookieStrategy(new MemoryCookieStrategy())
                // 只会对get请求应用服务器端设置的缓存头，5M大小
                .okHttpCache(getCacheDir(), 5 * 1024 * 1024)
                // 每个请求都具有的请求头，在这里可以统一配置
                .sharedHeadersProvider(new TestSharedHeadersProvider())
                .netConnectTimeout(60 * 1000)
                .netReadTimeout(60 * 1000)
                .netWriteTimeout(60 * 1000)
                .build());
    }
}