package com.base.library.rxRetrofit.http.cache

import com.base.library.rxRetrofit.common.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * 离线缓存处理
 *
 * @author  Alpinist Wang
 * Date:    2019-04-24
 */
class OfflineCacheInterceptor(private var offlineCacheTime: Int = 60) : Interceptor {

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