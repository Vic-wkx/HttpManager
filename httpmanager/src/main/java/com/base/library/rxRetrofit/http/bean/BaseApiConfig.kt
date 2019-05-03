package com.base.library.rxRetrofit.http.bean

/**
 * Description:
 * 全局BaseApi配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
open class BaseApiConfig {
    // Retrofit网络请求的BaseUrl
    open var baseUrl = "http://service.picasso.adesk.com/"
    // 是否显示Loading弹窗
    open var showLoading = true
    // Loading弹窗是否可取消
    open var loadingCancelable = true
    // 缓存配置
    open var cacheConfig = CacheConfig()
    // 是否忽略BaseResult判断
    open var ignoreJudge: Boolean = false
    // 重试配置
    open var retry = RetryConfig()
    // 超时时间配置
    open var timeOutConfig = TimeOutConfig()
}