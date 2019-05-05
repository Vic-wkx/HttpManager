package com.base.library.rxRetrofit.common.utils

import com.base.library.rxRetrofit.download.config.DownConfig
import com.base.library.rxRetrofit.download.bean.DownState
import com.base.library.rxRetrofit.download.bean.DownloadProgress

/**
 * Description:
 * 下载数据记录，使用的SP保存，可修改为使用数据库
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-30
 */
object DownRecordUtils {
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
        SPUtils.getInstance(DOWN_STATE).put(url, DownState.DOWNLOADING.state, true)
    }

    fun pause(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownState.PAUSE.state, true)
    }

    fun error(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownState.ERROR.state, true)
    }

    fun complete(url: String) {
        SPUtils.getInstance(DOWN_STATE).put(url, DownState.COMPLETE.state, true)
    }

    fun delete(url: String) {
        SPUtils.getInstance(READ).remove(url, true)
        SPUtils.getInstance(TOTAL).remove(url, true)
        SPUtils.getInstance(DOWN_STATE).remove(url, true)
    }

    fun isDownloading(config: DownConfig): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(config.url, DownState.UNKNOWN.state) ==
                DownState.DOWNLOADING.state
    }

    fun isPause(config: DownConfig): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(config.url, DownState.UNKNOWN.state) ==
                DownState.PAUSE.state
    }

    fun isCompleted(config: DownConfig): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(config.url, DownState.UNKNOWN.state) ==
                DownState.COMPLETE.state
    }

    fun isError(config: DownConfig): Boolean {
        return SPUtils.getInstance(DOWN_STATE).getInt(config.url, DownState.UNKNOWN.state) ==
                DownState.ERROR.state
    }

    fun getProgress(config: DownConfig): DownloadProgress {
        val read = SPUtils.getInstance(READ).getLong(config.url)
        val total = SPUtils.getInstance(TOTAL).getLong(config.url)
        return DownloadProgress(read, total)
    }

    fun getRange(url: String): Long {
        return SPUtils.getInstance(READ).getLong(url, 0L)
    }

}