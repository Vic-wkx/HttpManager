package com.base.library.rxRetrofit.http.config

import okhttp3.Response

/**
 * Description:
 * 默认的Http响应处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class DefaultHttpResponseFunc : IHttpResponseFunc {
    override fun handleResponse(response: Response): Response {
        // 在这里处理http返回的错误code，这里是Http响应的code，不同于BaseResult中的errorCode
        if (200 != response.code()) throw Throwable("Http response code = ${response.code()}")
        return response
    }

}