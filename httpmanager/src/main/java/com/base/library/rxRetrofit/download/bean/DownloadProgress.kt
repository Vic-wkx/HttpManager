package com.base.library.rxRetrofit.download.bean

import com.base.library.rxRetrofit.common.utils.ConvertUtils

/**
 * Description:
 * 下载进度
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-30
 */
class DownloadProgress(var read: Long, var total: Long) {
    val progress: Int
        get() = Math.round(read / (total * 1.0) * 100).toInt()

    /**
     * 内存大小进度
     */
    val memoryProgress: String
        get() = "${ConvertUtils.byte2FitMemorySize(read)} / ${ConvertUtils.byte2FitMemorySize(total)}"

    override fun toString(): String {
        return "$read / $total = $memoryProgress, progress = $progress%"
    }
}