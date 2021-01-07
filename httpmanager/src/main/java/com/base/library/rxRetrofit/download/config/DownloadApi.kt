package com.base.library.rxRetrofit.download.config

import android.net.Uri
import com.base.library.rxRetrofit.RxRetrofitApp

/**
 * Description:
 * 下载配置
 *
 * @author  Alpinist Wang
 * Date:    2019-04-26
 */
class DownloadApi {

    val downloadConfig = RxRetrofitApp.downConfig.copy()

    /**下载文件的url地址*/
    var url: String = ""
        get() {
            if (field.isEmpty()) throw NullPointerException("download url is empty")
            return field
        }

    /**保存的文件名字，如果不设置，默认名字是url的最后一段*/
    var saveFileName: String = ""
        get() {
            if (field.isNotEmpty()) return field
            if (url.isEmpty()) throw NullPointerException("download url is empty")
            return Uri.parse(url).lastPathSegment ?: ""
        }
    /**保存的文件全路径，由[downloadConfig.saveDir]和[saveFileName]拼接而成，不可手动设置*/
    val savePath: String
        get() = downloadConfig.saveDir + saveFileName

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DownloadApi) return false
        // 只要url相等，就判定为同一个DownConfig
        if (other.url == this.url) return true
        return false
    }
}