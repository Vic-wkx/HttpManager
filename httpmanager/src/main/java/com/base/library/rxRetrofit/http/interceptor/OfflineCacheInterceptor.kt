package com.base.library.rxRetrofit.http.interceptor

import com.base.library.rxRetrofit.http.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * 离线缓存处理
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-24
 */
class OfflineCacheInterceptor(var offlineCacheTime: Int = 60) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$offlineCacheTime")
                .build()
        }
        return chain.proceed(request)
    }
}