package com.base.library.rxRetrofit.http.down

import android.net.Uri
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.bean.RetryConfig

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
class DownConfig {

    var url: String = ""
    var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application?.externalCacheDir?.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }
    var saveFileName: String = ""
        get() {
            if (field.isNotEmpty() || url.isEmpty()) return field
            return Uri.parse(url).lastPathSegment ?: ""
        }
    val savePath: String
        get() = saveDir + saveFileName
    var retry = RetryConfig()

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