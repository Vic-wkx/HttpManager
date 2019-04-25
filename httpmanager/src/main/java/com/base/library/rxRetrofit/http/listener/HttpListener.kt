package com.base.library.rxRetrofit.http.listener

import io.reactivex.disposables.Disposable

/**
 * Description:
 * Http回调监听器
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019/4/23
 */
abstract class HttpListener {

    abstract fun onNext(result: String)

    abstract fun onError(error: Throwable)

    open fun onSubscribe(disposable: Disposable) {

    }

    open fun onComplete() {

    }
}