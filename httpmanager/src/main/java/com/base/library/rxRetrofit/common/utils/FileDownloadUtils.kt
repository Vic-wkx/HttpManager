package com.base.library.rxRetrofit.common.utils

import android.annotation.SuppressLint
import com.base.library.rxRetrofit.download.HttpDownManager
import com.base.library.rxRetrofit.download.bean.DownloadProgress
import com.base.library.rxRetrofit.download.config.DownConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Description:
 * 文件下载工具类
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-28
 */
object FileDownloadUtils {

    /**
     * 将输入流写入文件
     */
    @SuppressLint("CheckResult")
    fun writeCache(responseBody: ResponseBody, config: DownConfig, range: Long) {
        var randomAccessFile: RandomAccessFile? = null
        var inputStream: InputStream? = null
        try {
            val file = File(config.savePath)
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            inputStream = responseBody.byteStream()
            randomAccessFile = RandomAccessFile(file, "rwd")
            val total = range + responseBody.contentLength()
            DownRecordUtils.downloading(config.url)
            DownRecordUtils.saveTotal(config.url, total)
            randomAccessFile.setLength(total)
            randomAccessFile.seek(range)
            // 已读的字节数
            var read = range
            var lastDownloadProgress =
                DownloadProgress(read, total)
            val buffer = ByteArray(1024 * 4)
            // 每次读的大小并不是buffer的大小，所以需要将read的结果保存起来，加到read里面
            var len: Int
            while (true) {
                len = inputStream.read(buffer)
                if (len == -1 || !HttpDownManager.isDownloading(config)) break
                randomAccessFile.write(buffer, 0, len)
                read += len
                // 实时保存下载进度
                DownRecordUtils.saveRead(config.url, read)
                val downloadProgress =
                    DownloadProgress(read, total)
                if (notEnoughUpdateProgress(
                        config,
                        downloadProgress,
                        lastDownloadProgress
                    )
                ) continue
                Observable.just(downloadProgress)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (!HttpDownManager.isDownloading(config)) return@subscribe
                        HttpDownManager.getListener(config)?.onProgress(it)
                    }
                lastDownloadProgress = downloadProgress
            }
        } catch (e: IOException) {
            if (HttpDownManager.isDownloading(config)) throw Throwable("write to file Exception:$e")
        } finally {
            try {
                inputStream?.close()
                randomAccessFile?.close()
            } catch (e: IOException) {
                throw Throwable("close stream Exception in write to file:$e")
            }
        }
    }

    /**
     * 是否不足以更新进度
     */
    private fun notEnoughUpdateProgress(
        config: DownConfig,
        downloadProgress: DownloadProgress,
        lastDownloadProgress: DownloadProgress
    ): Boolean {
        if (config.progressStep == DownConfig.PROGRESS_BY_PERCENT) {
            // 如果是按照百分比更新进度，当前progress<=之前的progress，表示不足以更新进度，返回true
            if (downloadProgress.progress <= lastDownloadProgress.progress)
                return true
        } else {
            // 如果不是按照百分比更新进度，当前read的byte数-之前的read的byte数<progressStep，表示不足以更新进度，返回true
            if (downloadProgress.read - lastDownloadProgress.read < config.progressStep)
                return true
        }
        return false
    }
}