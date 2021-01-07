package com.base.library.rxRetrofit.common.header

import okhttp3.Request
import okhttp3.Response

/**
 * Description:
 * 默认的Http响应处理
 *
 * @author  Alpinist Wang
 * Date:    2019-05-04
 */
open class DefaultHttpResponseProcessor : IHttpResponseProcessor {
    override fun handleResponse(request: Request, response: Response): Response {
        // 在这里可以处理http返回的错误码：response.code()，这里的错误码不同于BaseResult中的errorCode
        // 例如：if (response.code >= 400) throw Throwable("Http response code = ${response.code}, request: ${request.url}")
        return response
    }
}