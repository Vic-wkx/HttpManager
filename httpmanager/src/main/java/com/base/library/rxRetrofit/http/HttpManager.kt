package com.base.library.rxRetrofit.http

import com.base.library.rxRetrofit.api.BaseApi
import com.base.library.rxRetrofit.http.func.ExceptionFunc
import com.base.library.rxRetrofit.http.func.ResultFunc
import com.base.library.rxRetrofit.http.func.RetryFunc
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers

/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
class HttpManager {

    private var observable: Observable<String>? = null
    private var activity: RxAppCompatActivity? = null
    private var fragment: RxFragment? = null
    // RxLifeCycle生命周期
    private val lifeCycle: LifecycleTransformer<String>
        get() {
            val fragmentLife = fragment?.bindUntilEvent<String>(FragmentEvent.DESTROY_VIEW)
            val activityLife = activity?.bindUntilEvent<String>(ActivityEvent.DESTROY)
            return fragmentLife ?: activityLife
            ?: throw NullPointerException("activity or fragment is null")
        }

    constructor(activity: RxAppCompatActivity) {
        this.activity = activity
    }

    constructor(fragment: RxFragment) {
        this.fragment = fragment
    }

    /**
     * 只接收onNext，onError回调
     */
    fun request(
        api: BaseApi,
        myOnNext: Consumer<String>,
        myOnError: Consumer<Throwable>
    ) {
        request(api, myOnNext, myOnError, Functions.EMPTY_ACTION, Functions.emptyConsumer<Disposable>())
    }

    /**
     * 接收onNext，OnError，onComplete，onStart回调
     */
    fun request(
        api: BaseApi,
        myOnNext: Consumer<String>,
        myOnError: Consumer<Throwable>,
        myOnComplete: Action,
        myOnSubscribe: Consumer<Disposable>
    ): Observable<String> {
        val observable = api.getObservable()
            /*返回数据统一判断*/
            .map(ResultFunc(api))
            /*失败后retry处理控制*/
            .retryWhen(RetryFunc(api.retry))
            /*错误包装处理*/
            .onErrorResumeNext(ExceptionFunc())
            /*http请求线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            /*绑定生命周期*/
            .compose(lifeCycle)
        val observer: Observer<String> = object : Observer<String> {
            override fun onComplete() {
                myOnComplete.run()
            }

            override fun onSubscribe(d: Disposable) {
                myOnSubscribe.accept(d)
            }

            override fun onNext(t: String) {
                myOnNext.accept(t)
            }

            override fun onError(e: Throwable) {
                myOnError.accept(e)
            }
        }
        observable?.subscribe(observer)
        return observable
    }
}