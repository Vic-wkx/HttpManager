package com.base.library.rxRetrofit.http.down

import io.reactivex.disposables.Disposable

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
abstract class HttpDownListener {

    abstract fun onProgress(read: Long, total: Long)

    abstract fun onComplete()

    abstract fun onError(e: Throwable)

    open fun onSubscribe(d: Disposable) {}

    open fun onPause() {}
}