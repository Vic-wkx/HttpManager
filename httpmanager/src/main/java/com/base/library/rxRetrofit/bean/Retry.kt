package com.base.library.rxRetrofit.common.bean

/**
 * Description:
 * 网络请求失败，重试配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class Retry {
    var count = 5
    var delay = 100L
    var increaseDelay = 100L
}