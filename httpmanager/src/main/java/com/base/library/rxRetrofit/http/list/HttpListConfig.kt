package com.base.library.rxRetrofit.http.list

/**
 * Description:
 * HttpList请求的配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
data class HttpListConfig(
        // 是否显示Loading弹窗
        var showLoading: Boolean = true,
        // Loading弹窗是否可取消
        var loadingCancelable: Boolean = true,
        // 是否按照顺序请求api
        var order: Boolean = false
)