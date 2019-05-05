package com.base.library.rxRetrofit.http.httpList

import com.base.library.rxRetrofit.RxRetrofitApp

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
        var showLoading: Boolean = RxRetrofitApp.httpListConfig.showLoading,
        // Loading弹窗是否可取消
        var loadingCancelable: Boolean = RxRetrofitApp.httpListConfig.loadingCancelable,
        // 是否按照顺序请求api
        var order: Boolean = RxRetrofitApp.httpListConfig.order
)