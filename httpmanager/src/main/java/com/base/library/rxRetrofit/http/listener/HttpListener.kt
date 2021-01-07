package com.base.library.rxRetrofit.http.listener

import io.reactivex.disposables.Disposable

/**
 * Description:
 * Http回调监听器
 *
 * @author  WZG
 * Date:    2019/4/23
 */
abstract class HttpListener {

    /**
     * 请求结果回调
     * @param result 请求结果字符串
     */
    abstract fun onNext(result: String)

    /**
     * 请求出错
     * @param error 错误信息
     */
    abstract fun onError(error: Throwable)

    /**
     * 请求开始
     */
    open fun onSubscribe(disposable: Disposable) {
    }

    /**
     * 请求完成
     */
    open fun onComplete() {
    }
}