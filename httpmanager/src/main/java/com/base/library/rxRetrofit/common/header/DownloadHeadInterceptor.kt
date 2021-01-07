package com.base.library.rxRetrofit.common.header

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * Http 下载 head 拦截器
 *
 * @author  WZG
 * Date:    2019-05-04
 */
class DownloadHeadInterceptor(private val headers: Headers?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        // 添加api中的header信息
        if (headers != null) requestBuilder.headers(headers)
        val request = requestBuilder.method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}
