package com.base.library.rxRetrofit.download.listener

import com.base.library.rxRetrofit.download.bean.DownloadProgress
import io.reactivex.disposables.Disposable

/**
 * Description:
 * 下载监听器
 *
 * @author  Alpinist Wang
 * Date:    2019-04-26
 */
abstract class DownloadListener {

    abstract fun onProgress(downloadProgress: DownloadProgress)

    abstract fun onComplete()

    abstract fun onError(e: Throwable)

    open fun onSubscribe(d: Disposable) {}

    open fun onPause() {}

    open fun onDelete() {}
}