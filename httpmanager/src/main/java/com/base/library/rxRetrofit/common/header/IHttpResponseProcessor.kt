package com.base.library.rxRetrofit.common.header

import okhttp3.Response

/**
 * Description:
 * Http响应码统一处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
interface IHttpResponseProcessor {
    /**
     * 处理Http响应码
     */
    fun handleResponse(response: Response): Response
}