package com.base.library.rxRetrofit.common.retry

/**
 * Description:
 * 网络请求失败时的重试配置
 *
 * @author  Alpinist Wang
 * Date:    2019/4/3
 */
data class RetryConfig(
    // 重试次数
    var count: Int = 5,
    // 重试延迟时间
    var delay: Long = 100L,
    // 每次增加延迟的时间
    var increaseDelay: Long = 500L
)