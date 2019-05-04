package com.base.library.rxRetrofit.http.config

/**
 * Description:
 * 超时时间配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
data class TimeOutConfig(
        // 连接超时时间
        var connectionTime: Long = 10L,
        // 读取超时时间
        var readTime: Long = 10L,
        // 写入超时时间
        var writeTime: Long = 10L
)