package io.github.tubb.explode;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.getkeepsafe.relinker.ReLinker;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static io.github.tubb.explode.CheckUtils.checkNotNull;
import static io.github.tubb.explode.CheckUtils.checkOnMainThread;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * Explode对外接口
 * Created by tubingbing on 18/3/30.
 */

public final class Explode {
    private static final Explode INSTANCE = new Explode();
    private ExplodeRetrofitGenerator mDefaultGenerator;
    private ExplodeConfig config;
    private ArrayMap<ExplodeRetrofitKey, ExplodeRetrofit> explodeRetrofitsCache = new ArrayMap<>();
    private ArrayMap<Long, HttpCanceler> httpCancelerMap = new ArrayMap<>();

    private Explode() {}

    /**
     * 获取Explode实例
     * @return Explode实例
     */
    public static Explode instance() {
        checkNotNull(INSTANCE.getConfig(), "Please init Explode first!");
        return INSTANCE;
    }

    /**
     * 初始化Explode
     * @param application Application
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public static void init(@NonNull Application application) {
        checkNotNull(application, "application == null");
        checkOnMainThread("Please execute on main thread");
        setUp(new ExplodeConfig.Builder(application).build());
    }

    /**
     * 初始化Explode
     * @param config ExplodeConfig
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public static void init(@NonNull ExplodeConfig config) {
        checkNotNull(config, "config == null");
        checkOnMainThread("Please execute on main thread");
        setUp(config);
    }

    private static void setUp(ExplodeConfig config) {
        ReLinker.loadLibrary(config.getContext(), "cookie-encrypt");
        INSTANCE.config = config;
        ExplodeLog.setDebug(INSTANCE.config.isDebug());
        INSTANCE.mDefaultGenerator = new DefaultExplodeRetrofitGenerator();
    }

    /**
     * 根据base url获取ExplodeRetrofit，缓存中存在，直接拿，不重新创建
     * @param baseUrl 基础url
     * @return ExplodeRetrofit
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public ExplodeRetrofit get(@NonNull final String baseUrl) {
        checkOnMainThread("Please execute on main thread");
        return get(checkNotNull(baseUrl, "Base URL required."), mDefaultGenerator);
    }

    /**
     * 根据base url和ExplodeRetrofitGenerator获取ExplodeRetrofit，缓存中存在，直接拿，不重新创建
     * @param baseUrl 基础url
     * @param generator 可定制化ExplodeRetrofit生成器
     * @return ExplodeRetrofit
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public ExplodeRetrofit get(@NonNull final String baseUrl,
                                    @NonNull final ExplodeRetrofitGenerator generator) {
        checkNotNull(baseUrl, "Base URL required.");
        checkNotNull(generator, "generator == null");
        checkOnMainThread("Please execute on main thread");
        ExplodeRetrofitKey explodeRetrofitKey = new ExplodeRetrofitKey(generator.id(), baseUrl);
        ExplodeRetrofit retrofit = findExplodeRetrofit(explodeRetrofitKey);
        if (isNull(retrofit)) {
            retrofit = generator.get(baseUrl);
            putExplodeRetrofit(explodeRetrofitKey, retrofit);
        }
        return retrofit;
    }

    /**
     * OkHttp3 Request为执行体，执行网络请求
     * @param request OkHttp3 Request
     * @param generator ExplodeRetrofit生成器，用来返回指定配置的OkHttpClient
     * @param converter Http response反序列化器
     * @param callback 结果回调
     * @param <W> 服务端响应的数据
     * @param <D> 业务数据
     * @return 每一次执行都被当做是做一次任务，返回任务id
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public <W, D> long execute(@NonNull final Request request,
                               @NonNull final ExplodeRetrofitGenerator generator,
                               @NonNull final ResponseConverter<W> converter,
                               @NonNull final HttpCallback<W, D> callback) {
        checkNotNull(request, "request == null");
        checkNotNull(generator, "generator == null");
        checkNotNull(converter, "converter == null");
        checkNotNull(callback, "callback == null");
        checkOnMainThread("Please execute on main thread");
        final okhttp3.Call call = generator.getOkHttpClient().newCall(request);
        Observable<W> observable = Observable.create(new ObservableOnSubscribe<W>() {
            @Override
            public void subscribe(ObservableEmitter<W> e) throws Exception {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (!isNull(responseBody)) {
                        W data = converter.convert(responseBody.string());
                        if (!isNull(data)) {
                            e.onNext(data);
                        } else {
                            e.onError(new NullPointerException("Null is not a valid element"));
                        }
                    } else {
                        e.onError(new NullPointerException("Null is not a valid element"));
                    }
                } else {
                    e.onError(new RuntimeException("HTTP " + response.code() + " " + response.message()));
                }
            }
        });
        return execute(observable, callback);
    }

    /**
     * OkHttp3 Request为执行体，执行网络请求
     * @param request OkHttp3 Request
     * @param converter Http response反序列化器
     * @param callback 结果回调
     * @param <W> 服务端响应的数据
     * @param <D> 业务数据
     * @return 每一次执行都被当做是做一次任务，返回任务id
     */
    @MainThread
    public <W, D> long execute(@NonNull final Request request,
                               @NonNull final ResponseConverter<W> converter,
                               @NonNull final HttpCallback<W, D> callback) {
        return execute(request, mDefaultGenerator, converter, callback);
    }

    /**
     * Retrofit Call为执行体，执行网络请求
     * @param finalCall Retrofit Call
     * @param callback 结果回调
     * @param <W> 服务端响应的数据
     * @param <D> 业务数据
     * @return 每一次执行都被当做是做一次任务，返回任务id
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public <W, D> long execute(@NonNull final Call<W> finalCall,
                               @NonNull final HttpCallback<W, D> callback) {
        checkNotNull(finalCall, "finalCall == null");
        checkNotNull(callback, "callback == null");
        checkOnMainThread("Please execute on main thread");
        return execute(Observable.create(new ObservableOnSubscribe<W>() {
            @Override
            public void subscribe(ObservableEmitter<W> e) throws Exception {
                e.onNext(finalCall.execute().body());
            }
        }), callback);
    }

    /**
     * RxJava Observable为执行体，执行网络请求
     * @param finalObservable 执行体
     * @param callback 结果回调
     * @param <W> 服务端响应的数据
     * @param <D> 业务数据
     * @return 每一次执行都被当做是做一次任务，返回任务id
     * @throws RuntimeException if execute not in main thread
     */
    @MainThread
    public <W, D> long execute(@NonNull final Observable<W> finalObservable,
                               @NonNull final HttpCallback<W, D> callback) {
        checkNotNull(finalObservable, "finalObservable == null");
        checkNotNull(callback, "callback == null");
        checkOnMainThread("Please execute on main thread");
        callback.onStart();
        final long taskId = System.nanoTime();
        Disposable disposable = finalObservable
                .compose(new NetworkThreadTransformer<W>())
                .subscribe(new Consumer<W>() {
                    @Override
                    public void accept(W data) throws Exception {
                        if (!isTaskCanceled(taskId)) {
                            callback.beforeOnSuccess(data);
                            callback.onFinish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isTaskCanceled(taskId)) {
                            callback.beforeOnError(throwable);
                            callback.onFinish();
                        }
                    }
                });
        httpCancelerMap.put(taskId, new HttpCanceler<>(disposable, callback));
        return taskId;
    }

    private boolean isTaskCanceled(final long taskId) {
        HttpCanceler canceler = httpCancelerMap.get(taskId);
        return !isNull(canceler) && canceler.isCanceled();
    }

    /**
     * 取消任务，业务层将得不到结果回调
     * @param taskId 任务id
     */
    @MainThread
    public void cancel(final long taskId) {
        checkOnMainThread("Please execute on main thread");
        HttpCanceler canceler = httpCancelerMap.get(taskId);
        if (!isNull(canceler))
            canceler.cancel();
        else {
            if (ExplodeLog.DEBUG)
                ExplodeLog.w("Can't find HttpCanceler for task id: " + taskId);
        }
        httpCancelerMap.remove(taskId);
    }

    private void putExplodeRetrofit(ExplodeRetrofitKey key, ExplodeRetrofit retrofit) {
        explodeRetrofitsCache.put(key, retrofit);
    }

    @Nullable
    private ExplodeRetrofit findExplodeRetrofit(ExplodeRetrofitKey key) {
        return explodeRetrofitsCache.get(key);
    }

    ExplodeConfig getConfig() {
        return config;
    }
}
