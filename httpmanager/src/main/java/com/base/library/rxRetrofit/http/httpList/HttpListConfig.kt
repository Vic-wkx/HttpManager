package com.base.library.rxRetrofit.http.httpList

/**
 * Description:
 * HttpList请求的配置
 *
 * @author  Alpinist Wang
 * Date:    2019-04-25
 */
open class HttpListConfig {
    // 是否显示Loading弹窗
    open var showLoading: Boolean = true

    // Loading弹窗是否可取消
    open var loadingCancelable: Boolean = true

    // 是否按照顺序请求api
    open var order: Boolean = true
}