package com.base.library.rxRetrofit.http

import android.content.Context
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.func.ResultFunc
import com.base.library.rxRetrofit.http.func.RetryFunc
import com.base.library.rxRetrofit.http.list.HttpListConfig
import com.base.library.rxRetrofit.http.listener.HttpListListener
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.base.library.rxRetrofit.http.observer.HttpListObserver
import com.base.library.rxRetrofit.http.observer.HttpObserver
import com.base.library.rxRetrofit.http.utils.bind
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable

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
        get() = activity ?: fragment?.context ?: RxRetrofitApp.application?.applicationContext
        ?: throw Throwable("context is null")

    constructor(activity: RxAppCompatActivity) {
        this.activity = activity
    }

    constructor(fragment: RxFragment) {
        this.fragment = fragment
    }

    /**
     * 如果不传Activity和Fragment，默认使用application的Context
     */
    constructor()

    fun request(api: BaseApi, listener: HttpListener) {
        api.getObservable()
            /*失败后retry处理控制*/
            .retryWhen(RetryFunc(api.retry))
            /*返回数据统一判断*/
            .map(ResultFunc(api))
            .bind(fragment, activity)
            .subscribe(HttpObserver(context, api, listener))
    }

    fun request(apis: Array<BaseApi>, config: HttpListConfig = HttpListConfig(), listener: HttpListListener) {
        val resultMap = HashMap<BaseApi, Any>()
        val observable = with(Observable.fromArray(*apis)) {
            if (config.order)
                this.concatMap {
                    requestSingleApi(it, resultMap, listener)
                }
            else
                this.flatMap {
                    requestSingleApi(it, resultMap, listener)
                }
        }
        observable.buffer(apis.size)
            .bind(fragment, activity)
            .subscribe(HttpListObserver(context, resultMap, config, listener))
    }

    private fun requestSingleApi(
        api: BaseApi,
        resultMap: HashMap<BaseApi, Any>,
        listener: HttpListListener
    ): Observable<Unit> {
        return api.getObservable()
            /*失败后retry处理控制*/
            .retryWhen(RetryFunc(api.retry))
            /*返回数据统一判断*/
            .map(ResultFunc(api))
            .map {
                resultMap[api] = listener.onSingleNext(api, it)
            }
            .bind(fragment, activity)
    }
}