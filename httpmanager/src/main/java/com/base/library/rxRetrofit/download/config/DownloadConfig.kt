package com.base.library.rxRetrofit.download.config

import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.common.retry.RetryConfig
import okhttp3.Headers

/**
 * Description:
 * 默认下载配置
 *
 * @author  Alpinist Wang
 * Date:    2019-05-05
 */
open class DownloadConfig : Cloneable {
    companion object {
        // 下载进度更新频率，此值表示按照进度的百分之一更新
        const val PROGRESS_BY_PERCENT = -1
    }

    /**保存的文件夹路径，如果不设置，默认路径是"应用缓存路径/download/",如果设置为外部路径，需要自己确保有读写权限*/
    open var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application.externalCacheDir?.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }

    /**下载进度更新频率，即下载多少B之后更新一次，默认4K。使用[PROGRESS_BY_PERCENT]表示按百分比更新*/
    open var progressStep = 1024 * 4

    /**重试配置*/
    open var retry = RetryConfig()

    /**head信息*/
    open var headers: Headers? = null

    fun copy(): DownloadConfig {
        return clone() as DownloadConfig
    }
}