package com.base.library.rxRetrofit.http.httpList

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Description:
 * HttpList请求结果观察者
 *
 * @author  Alpinist Wang
 * Date:    2019-04-25
 */
class HttpListObserver(
    private val activity: AppCompatActivity?,
    private val fragment: Fragment?,
    private val context: Context,
    private val resultMap: HashMap<BaseApi, Any>,
    private val config: HttpListConfig,
    private val listener: HttpListListener
) : Observer<List<Unit>>, DefaultLifecycleObserver {

    var loading: ProgressDialog? = null
    var disposable: Disposable? = null


    override fun onSubscribe(d: Disposable) {
        disposable = d
        activity?.lifecycle?.addObserver(this)
        fragment?.lifecycle?.addObserver(this)
        listener.onSubscribe(d)
        showLoadingIfNeed()
    }

    private fun showLoadingIfNeed() {
        if (!config.showLoading || context == RxRetrofitApp.application.applicationContext) return
        if (loading == null) {
            loading =
                ProgressDialog.show(context, null, "Loading", false, config.loadingCancelable) {
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

    override fun onDestroy(owner: LifecycleOwner) {
        disposable?.dispose()
        super.onDestroy(owner)
    }
}