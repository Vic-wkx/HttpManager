package com.base.library.rxRetrofit.http.interceptor

import com.base.library.rxRetrofit.RxRetrofitApp
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * Http请求head拦截器
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
        return RxRetrofitApp.httpResponseFunc.handleResponse(response)
    }
}
