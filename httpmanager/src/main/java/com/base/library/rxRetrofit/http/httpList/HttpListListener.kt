package com.base.library.rxRetrofit.http.httpList

import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.disposables.Disposable

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
abstract class HttpListListener {

    /**
     * 收到了所有结果
     * resultMap：api -> 请求结果
     */
    abstract fun onNext(resultMap: HashMap<BaseApi, Any>)

    /**
     * 请求出错
     * error：HttpList请求错误信息
     */
    abstract fun onError(error: Throwable)

    /**
     * 收到了单个api的结果
     */
    open fun onSingleNext(api: BaseApi, result: String): Any {
        return result
    }

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