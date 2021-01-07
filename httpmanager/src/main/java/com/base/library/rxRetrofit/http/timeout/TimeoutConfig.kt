package com.base.library.rxRetrofit.http.timeout

/**
 * Description:
 * 超时时间配置
 *
 * @author  Alpinist Wang
 * Date:    2019-04-25
 */
data class TimeoutConfig(
    // 连接超时时间
    var connectionTime: Long = 10L,
    // 读取超时时间
    var readTime: Long = 10L,
    // 写入超时时间
    var writeTime: Long = 10L
)