package com.base.library.rxRetrofit.http.config

import okhttp3.Response

/**
 * Description:
 * Http错误码处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
interface IHttpResponseFunc {
    /**
     * 处理Http错误码
     */
    fun handleResponse(response: Response): Response
}