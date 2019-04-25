package com.base.library.rxRetrofit.http.observer

import android.app.ProgressDialog
import android.content.Context
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.list.HttpListConfig
import com.base.library.rxRetrofit.http.listener.HttpListListener
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Description:
 * HttpList请求结果观察者
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class HttpListObserver(
        private val context: Context,
        private val resultMap: HashMap<BaseApi, Any>,
        private val listener: HttpListListener,
        private val config: HttpListConfig
) :
        Observer<List<Unit>> {

    var loading: ProgressDialog? = null
    var disposable: Disposable? = null


    override fun onSubscribe(d: Disposable) {
        disposable = d
        listener.onSubscribe(d)
        showLoadingIfNeed()
    }

    private fun showLoadingIfNeed() {
        if (!config.showLoading) return
        if (loading == null) {
            loading = ProgressDialog.show(context, null, "Loading", false, config.loadingCancelable) {
                disposable?.dispose()
                listener.onError(Throwable("request cancel"))
            }
        } else {
            loading?.show()
        }
    }

    override fun onNext(ignore: List<Unit>) {
        listener.onNext(resultMap)
    }

    override fun onError(error: Throwable) {
        loading?.dismiss()
        listener.onError(error)
    }

    override fun onComplete() {
        loading?.dismiss()
        listener.onComplete()
    }
}