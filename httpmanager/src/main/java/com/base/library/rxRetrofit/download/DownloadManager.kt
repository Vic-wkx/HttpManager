package com.base.library.rxRetrofit.download

import com.base.library.rxRetrofit.common.extension.bindIOToMainThread
import com.base.library.rxRetrofit.common.header.DownloadHeadInterceptor
import com.base.library.rxRetrofit.common.retry.RetryFunction
import com.base.library.rxRetrofit.common.utils.DownloadRecordUtils
import com.base.library.rxRetrofit.common.utils.FileDownloadUtils
import com.base.library.rxRetrofit.common.utils.FileUtils
import com.base.library.rxRetrofit.common.utils.UrlUtils
import com.base.library.rxRetrofit.download.bean.DownloadProgress
import com.base.library.rxRetrofit.download.config.DownloadApi
import com.base.library.rxRetrofit.download.listener.DownloadListener
import com.base.library.rxRetrofit.download.service.DownloadService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Description:
 * DownManager，用来下载网络文件
 *
 * @author  Alpinist Wang
 * Date:    2019-04-26
 */
@Suppress("CheckResult", "unused")
object DownloadManager {

    // 下载监听器列表
    private val listenerMap = HashMap<DownloadApi, DownloadListener>()

    // 下载任务列表
    private val downloadingDisposables = HashMap<DownloadApi, Disposable>()

    fun download(api: DownloadApi) {
        if (api.url.isEmpty()) throw Throwable("download url is empty")
        // 防止多次下载同一个文件
        if (isDownloading(api)) return
        // 创建retrofit对象
        val builder = OkHttpClient.Builder()
            .addInterceptor(DownloadHeadInterceptor(api.downloadConfig.headers))
        val retrofit = Retrofit.Builder()
            .client(builder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(UrlUtils.getBaseUrl(api.url))
            .build()
        // 查询记录的下载进度
        val range = DownloadRecordUtils.getRange(api.url)
        val disposable = retrofit.create(DownloadService::class.java)
            .download("bytes=$range-", api.url)
            .retryWhen(RetryFunction(api.downloadConfig.retry))
            .map {
                FileDownloadUtils.writeCache(it, api, range)
            }
            .bindIOToMainThread()
            .subscribe({
                DownloadRecordUtils.complete(api.url)
            }, {
                listenerMap[api]?.onError(it)
                DownloadRecordUtils.error(api.url)
            }, {
                listenerMap[api]?.onComplete()
            }, {
                DownloadRecordUtils.downloading(api.url)
                listenerMap[api]?.onSubscribe(it)
            })
        downloadingDisposables[api] = disposable
    }

    fun pause(api: DownloadApi) {
        if (!isDownloading(api)) return
        downloadingDisposables[api]?.dispose()
        listenerMap[api]?.onPause()
        DownloadRecordUtils.pause(api.url)
    }

    fun delete(
        api: DownloadApi
    ) {
        Observable.just(api)
            .map {
                downloadingDisposables[it]?.dispose()
                FileUtils.delete(it.savePath)
                DownloadRecordUtils.delete(it.url)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listenerMap[api]?.onDelete()
            }, {
                listenerMap[api]?.onError(it)
            })
    }

    fun isDownloading(api: DownloadApi): Boolean {
        return downloadingDisposables[api] != null
                && downloadingDisposables[api]?.isDisposed == false
                && DownloadRecordUtils.isDownloading(api)
    }

    fun getListener(api: DownloadApi): DownloadListener? {
        return listenerMap[api]
    }

    fun bindListener(api: DownloadApi, listener: DownloadListener) {
        if (api.url.isEmpty()) throw Throwable("download url is empty")
        listenerMap[api] = listener
    }

    fun unbindListener(api: DownloadApi) {
        listenerMap.remove(api)
    }

    fun isCompleted(api: DownloadApi): Boolean {
        return DownloadRecordUtils.isCompleted(api)
    }

    fun isPause(api: DownloadApi): Boolean {
        return DownloadRecordUtils.isPause(api)
    }

    fun isError(api: DownloadApi): Boolean {
        return DownloadRecordUtils.isError(api)
    }

    fun getProgress(api: DownloadApi): DownloadProgress {
        return DownloadRecordUtils.getProgress(api)
    }

}