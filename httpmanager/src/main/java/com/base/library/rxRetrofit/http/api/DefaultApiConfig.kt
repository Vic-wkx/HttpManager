package com.base.library.rxRetrofit.http.api

import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.common.retry.RetryConfig
import com.base.library.rxRetrofit.http.cache.CacheConfig
import com.base.library.rxRetrofit.http.timeout.TimeoutConfig
import okhttp3.Headers

/**
 * Description:
 * 全局BaseApi配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
open class DefaultApiConfig {
    // Retrofit网络请求的BaseUrl
    open var baseUrl = ""
    // 是否显示Loading弹窗
    open var showLoading = true
    // Loading弹窗是否可取消
    open var loadingCancelable = true
    // 缓存配置
    open var cacheConfig = CacheConfig()
    // 是否忽略ResultConverter解析
    open var ignoreResultConverter: Boolean = false
    // 是否忽略ResponseProcessor对返回结果的处理
    open var ignoreResponseProcessor: Boolean = false
    // 重试配置
    open var retry = RetryConfig()
    // 超时时间配置
    open var timeOutConfig = TimeoutConfig()
    // Http请求head信息
    open var headers: Headers? = null
}