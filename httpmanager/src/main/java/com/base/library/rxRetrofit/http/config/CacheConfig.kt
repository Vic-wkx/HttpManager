package com.base.library.rxRetrofit.http.config

/**
 * Description:
 * 网络请求缓存配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
data class CacheConfig(
        // 是否需要缓存处理
        var cache: Boolean = false,
        // 有网的时候的缓存过期时间
        var onlineCacheTime: Int = 30,
        // 没网的时候的缓存过期时间
        var offlineCacheTime: Int = 60 * 60 * 24 * 30
)