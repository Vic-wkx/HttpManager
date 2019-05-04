package com.base.library.rxRetrofit.http.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * 统一的head拦截器处理
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class HeadInterceptor(private val headers: Headers?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        if (headers != null) requestBuilder.headers(headers)
        val request = requestBuilder.method(original.method(), original.body())
            .build()
        val response = chain.proceed(request)
        // 在这里处理http返回的错误code，这里是Http响应的code，不同于BaseResult中的errorCode
        if (200 != response.code()) throw Throwable("Http response code = ${response.code()}")
        return response
    }
}
