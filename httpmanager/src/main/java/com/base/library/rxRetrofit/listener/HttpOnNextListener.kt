package com.base.library.rxRetrofit.listener

/**
 * Http结果回调
 * Created by WZG on 2016/7/16.
 */
interface HttpOnNextListener {
    /**
     * 成功回调
     * @param result
     * @param method
     */
    fun onNext(result: String, method: String)

    /**
     * 失败回调
     * @param e
     * @param method
     */
    fun onError(e: Throwable?, method: String)
}
