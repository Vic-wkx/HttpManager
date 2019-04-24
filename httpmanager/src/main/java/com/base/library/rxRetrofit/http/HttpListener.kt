package com.base.library.rxRetrofit.http

import io.reactivex.disposables.Disposable

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/23
 */
abstract class HttpListener{

    abstract fun onNext(result: String)

    abstract fun onError(error: Throwable)

    fun onSubscribe(disposable: Disposable){

    }
    fun onComplete(){

    }
}