package com.base.library.rxRetrofit.http.utils

/**
 * Description:
 * 下载状态
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-30
 */
enum class DownState(var state: Int) {
    DOWNLOADING(0),
    COMPLETE(1),
    PAUSE(2),
    ERROR(3),
    UNKNOWN(-1)
}