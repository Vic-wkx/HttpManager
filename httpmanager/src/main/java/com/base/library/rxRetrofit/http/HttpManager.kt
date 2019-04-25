package com.base.library.rxRetrofit.http

import android.content.Context
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.func.ResultFunc
import com.base.library.rxRetrofit.http.func.RetryFunc
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.base.library.rxRetrofit.http.observer.HttpObserver
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * HttpManager
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class HttpManager {

    private var activity: RxAppCompatActivity? = null
    private var fragment: RxFragment? = null
    private val context: Context
        get() = activity ?: fragment?.context ?: throw Throwable("activity or fragment is null")
    // RxLifeCycle生命周期
    private val lifeCycle: LifecycleTransformer<String>
        get() {
            val fragmentLife = fragment?.bindUntilEvent<String>(FragmentEvent.DESTROY_VIEW)
            val activityLife = activity?.bindUntilEvent<String>(ActivityEvent.DESTROY)
            return fragmentLife ?: activityLife
            ?: throw Throwable("activity or fragment is null")
        }

    constructor(activity: RxAppCompatActivity) {
        this.activity = activity
    }

    constructor(fragment: RxFragment) {
        this.fragment = fragment
    }

    fun request(api: BaseApi, listener: HttpListener) {
        api.getObservable()
            /*失败后retry处理控制*/
            .retryWhen(RetryFunc(api.retry))
            /*返回数据统一判断*/
            .map(ResultFunc(api))
            /*http请求线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            /*绑定生命周期*/
            .compose(lifeCycle)
            .subscribe(HttpObserver(context, api, listener))
    }
}