package com.base.library.rxRetrofit.http.observer

import com.base.library.rxRetrofit.http.api.BaseApi
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
    private val resultMap: HashMap<BaseApi, Any>,
    private val errorMap: HashMap<BaseApi, Throwable>,
    private val listener: HttpListListener
) :
    Observer<List<Unit>> {
    override fun onComplete() {
        listener.onComplete()
    }

    override fun onSubscribe(disposable: Disposable) {
        listener.onSubscribe(disposable)
    }

    override fun onNext(ignore: List<Unit>) {
        if (errorMap.isNotEmpty()) {
            listener.onError(Throwable("One or more requests went wrong. See errorMap for details."), errorMap)
            return
        }
        listener.onNext(resultMap)
    }

    override fun onError(error: Throwable) {
        listener.onError(error, errorMap)
    }
}