package com.base.library.rxRetrofit.common.utils

import com.base.library.rxRetrofit.download.config.DownloadApi
import com.base.library.rxRetrofit.download.bean.DownloadState
import com.base.library.rxRetrofit.download.bean.DownloadProgress

/**
 * Description:
 * 下载数据记录，使用的SP保存，可修改为使用数据库
 *
 * @author  Alpinist Wang
 * Date:    2019-04-30
 */
object DownloadRecordUtils {
    private const val READ = "read"
    private const val TOTAL = "total"
    private const val DOWN_STATE = "downState"

    fun saveRead(url: String, read: Long) {
        SPUtils.getInstance(READ).put(url, read, true)
    }

    fun saveTotal(url: String, total: Long) {
        SPUtils.getInstance(TOTAL).put(url, total, true)
    }

    fun downloading(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownloadState.DOWNLOADING.state, true)
    }

    fun pause(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownloadState.PAUSE.state, true)
    }

    fun error(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownloadState.ERROR.state, true)
    }

    fun complete(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownloadState.COMPLETE.state, true)
    }

    fun delete(url: String) {
        SPUtils.getInstance(READ).remove(url, true)
        SPUtils.getInstance(TOTAL).remove(url, true)
        SPUtils.getInstance(DOWN_STATE).remove(url, true)
    }

    fun isDownloading(api: DownloadApi): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(api.url, DownloadState.UNKNOWN.state) ==
                DownloadState.DOWNLOADING.state
    }

    fun isPause(api: DownloadApi): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(api.url, DownloadState.UNKNOWN.state) ==
                DownloadState.PAUSE.state
    }

    fun isCompleted(api: DownloadApi): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(api.url, DownloadState.UNKNOWN.state) ==
                DownloadState.COMPLETE.state
    }

    fun isError(api: DownloadApi): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(api.url, DownloadState.UNKNOWN.state) ==
                DownloadState.ERROR.state
    }

    fun getProgress(api: DownloadApi): DownloadProgress {
        val read = SPUtils.getInstance(READ).getLong(api.url)
        val total = SPUtils.getInstance(TOTAL).getLong(api.url)
        return DownloadProgress(read, total)
    }

    fun getRange(url: String): Long {
        return SPUtils.getInstance(READ).getLong(url, 0L)
    }

}