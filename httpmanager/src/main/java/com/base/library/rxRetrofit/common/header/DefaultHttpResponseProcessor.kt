package com.base.library.rxRetrofit.common.header

import okhttp3.Response

/**
 * Description:
 * 默认的Http响应处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class DefaultHttpResponseProcessor : IHttpResponseProcessor {
    override fun handleResponse(response: Response): Response {
        // 在这里可以处理http返回的错误码：response.code()，这里的错误码不同于BaseResult中的errorCode
        if (response.code() >= 400) throw Throwable("Http response code = ${response.code()}")
        return response
    }
}