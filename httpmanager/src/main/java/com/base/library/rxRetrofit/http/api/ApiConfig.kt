package com.base.library.rxRetrofit.http.api

import com.base.library.rxRetrofit.common.header.DefaultHttpResponseProcessor
import com.base.library.rxRetrofit.common.header.IHttpResponseProcessor
import com.base.library.rxRetrofit.common.retry.RetryConfig
import com.base.library.rxRetrofit.http.cache.CacheConfig
import com.base.library.rxRetrofit.http.converter.DefaultResultConverter
import com.base.library.rxRetrofit.http.converter.IResultConverter
import com.base.library.rxRetrofit.http.timeout.TimeoutConfig
import okhttp3.Headers

/**
 * Description:
 * 全局BaseApi配置
 *
 * @author  Alpinist Wang
 * Date:    2019-05-04
 */
open class ApiConfig : Cloneable {
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

    // 返回数据统一转换类
    open var resultConverter: IResultConverter = DefaultResultConverter()

    // Http 响应统一处理类
    open var httpResponseProcessor: IHttpResponseProcessor = DefaultHttpResponseProcessor()

    fun copy(): ApiConfig {
        // kotlin 中默认为深拷贝
        return clone() as ApiConfig
    }
}