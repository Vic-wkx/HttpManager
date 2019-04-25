package com.base.library.rxRetrofit.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * 有网时的缓存处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-24
 */
class NetCacheInterceptor(private val onlineCacheTime: Int = 30) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$onlineCacheTime")
            .removeHeader("Pragma")
            .build()
    }
}