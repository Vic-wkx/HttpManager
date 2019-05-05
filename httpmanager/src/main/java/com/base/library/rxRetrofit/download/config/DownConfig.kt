package com.base.library.rxRetrofit.download.config

import android.net.Uri
import com.base.library.rxRetrofit.RxRetrofitApp
import okhttp3.Headers

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
class DownConfig {

    companion object {
        // 下载进度更新频率，此值表示按照进度的百分之一更新
        const val PROGRESS_BY_PERCENT = -1
    }

    /**下载文件的url地址*/
    var url: String = ""
        get() {
            if (url.isEmpty()) throw NullPointerException("download url is empty")
            return field
        }
    /**保存的文件夹路径，如果不设置，默认路径是"应用缓存路径/download",如果设置为外部路径，需要自己确保有读写权限*/
    var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            return RxRetrofitApp.downConfig.saveDir
        }
    /**保存的文件名字，如果不设置，默认名字是url的最后一段*/
    var saveFileName: String = ""
        get() {
            if (field.isNotEmpty()) return field
            if (url.isEmpty()) throw NullPointerException("download url is empty")
            return Uri.parse(url).lastPathSegment ?: ""
        }
    /**下载进度更新频率，即下载多少B之后更新一次，使用[PROGRESS_BY_PERCENT]表示按百分比更新*/
    var progressStep = RxRetrofitApp.downConfig.progressStep
    /**重试配置*/
    var retry = RxRetrofitApp.downConfig.retry
    /**head信息*/
    var headers: Headers? = RxRetrofitApp.downConfig.headers
    /**保存的文件全路径，由[saveDir]和[saveFileName]拼接而成，不可手动设置*/
    val savePath: String
        get() = saveDir + saveFileName

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DownConfig) return false
        // 只要url相等，就判定为同一个DownConfig
        if (other.url == this.url) return true
        return false
    }
}