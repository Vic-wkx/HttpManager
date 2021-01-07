package com.base.library.rxRetrofit.download.bean

/**
 * Description:
 * 下载状态
 *
 * @author  Alpinist Wang
 * Date:    2019-04-30
 */
enum class DownloadState(var state: Int) {
    DOWNLOADING(0),
    COMPLETE(1),
    PAUSE(2),
    ERROR(3),
    UNKNOWN(-1)
}