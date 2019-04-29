package com.base.library.rxRetrofit.http.utils

import android.util.Log
import com.base.library.rxRetrofit.http.down.DownConfig
import com.base.library.rxRetrofit.http.down.HttpDownListener
import com.base.library.rxRetrofit.http.down.HttpDownManager
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-28
 */
object FileUtils {

    fun writeCache(
        responseBody: ResponseBody,
        config: DownConfig,
        range: Long,
        listener: HttpDownListener?
    ) {
        var randomAccessFile: RandomAccessFile? = null
        var inputStream: InputStream? = null
        try {
            val file = File(config.savePath)
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            inputStream = responseBody.byteStream()
            randomAccessFile = RandomAccessFile(file, "rwd")
            val totalLength = range + responseBody.contentLength()
            randomAccessFile.setLength(totalLength)
            randomAccessFile.seek(range)
            // 已读的字节数
            var read = range
            val buffer = ByteArray(1024 * 4)
            // 每次读的大小并不是buffer的大小，所以需要将read的结果保存起来，加到read里面
            var len: Int
            while (true) {
                len = inputStream.read(buffer)
                if (len == -1 || !HttpDownManager.isDownloading(config.url)) break
                randomAccessFile.write(buffer, 0, len)

                read += len
                // 实时保存下载进度到SP中
                SPUtils.getInstance().put(config.url, read, true)
                if (!HttpDownManager.isDownloading(config.url)) {
                    Log.e("~~~", "isDownloading = false")
                    break
                }
                listener?.onProgress(read, totalLength)
            }
        } catch (e: IOException) {
            if (!HttpDownManager.isDownloading(config.url)) {
                Log.e("~~~", "write to file Exception:$e")
                return
            }
            throw Throwable("write to file Exception:$e")
        } finally {
            try {
                inputStream?.close()
                randomAccessFile?.close()
            } catch (e: IOException) {
                throw Throwable("close stream Exception in write to file:$e")
            }
        }
    }
}