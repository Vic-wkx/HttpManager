package com.base.library.rxRetrofit.http.observer

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.listener.HttpListener
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Description:
 * Http请求结果观察者
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019/4/23
 */
@SuppressLint("CheckResult")
class HttpObserver(
    private val context: Context,
    private val api: BaseApi,
    private val listener: HttpListener
) :
    Observer<String> {

    var loading: ProgressDialog? = null
    var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
        listener.onSubscribe(d)
        showLoadingIfNeed()
    }

    private fun showLoadingIfNeed() {
        // 如果使用的系统application的Context，不允许弹窗
        if (!api.showLoading || context == RxRetrofitApp.application?.applicationContext) return
        if (loading == null) {
            loading = ProgressDialog.show(context, null, "Loading", false, api.loadingCancelable) {
                disposable?.dispose()
                listener.onError(Throwable("request cancel"))
            }
        } else {
            loading?.show()
        }
    }

    override fun onNext(t: String) {
        listener.onNext(t)
    }

    override fun onError(e: Throwable) {
        loading?.dismiss()
        listener.onError(e)
    }

    override fun onComplete() {
        loading?.dismiss()
        listener.onComplete()
    }
}