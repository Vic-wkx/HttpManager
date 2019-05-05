package com.base.library.rxRetrofit.http.httpList

/**
 * Description:
 * 默认HttpList配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-05
 */
open class DefaultHttpListConfig {
    // 是否显示Loading弹窗
    open var showLoading: Boolean = true
    // Loading弹窗是否可取消
    open var loadingCancelable: Boolean = true
    // 是否按照顺序请求api
    open var order: Boolean = false
}