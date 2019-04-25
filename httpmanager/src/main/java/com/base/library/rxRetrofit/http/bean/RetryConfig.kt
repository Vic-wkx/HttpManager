package com.base.library.rxRetrofit.http.bean

/**
 * Description:
 * 网络请求失败时的重试配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class RetryConfig {
    // 重试次数
    var count = 5
    // 重试延迟时间
    var delay = 100L
    // 每次增加延迟的时间
    var increaseDelay = 100L
}