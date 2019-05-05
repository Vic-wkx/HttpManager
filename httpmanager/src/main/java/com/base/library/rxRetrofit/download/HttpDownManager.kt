package com.base.library.rxRetrofit.download

import android.annotation.SuppressLint
import com.base.library.rxRetrofit.common.extension.bindIOToMainThread
import com.base.library.rxRetrofit.common.header.HeadInterceptor
import com.base.library.rxRetrofit.common.retry.RetryFunction
import com.base.library.rxRetrofit.common.utils.DownRecordUtils
import com.base.library.rxRetrofit.common.utils.FileDownloadUtils
import com.base.library.rxRetrofit.common.utils.FileUtils
import com.base.library.rxRetrofit.common.utils.UrlUtils
import com.base.library.rxRetrofit.download.bean.DownloadProgress
import com.base.library.rxRetrofit.download.config.DownConfig
import com.base.library.rxRetrofit.download.listener.HttpDownListener
import com.base.library.rxRetrofit.download.service.HttpDownService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Description:
 * HttpDownManager，用来下载网络文件
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
@SuppressLint("CheckResult")
object HttpDownManager {

    // 下载监听器列表
    private val listeners = HashMap<DownConfig, HttpDownListener>()
    // 下载任务列表
    private val downloadingDisposables = HashMap<DownConfig, Disposable>()

    fun down(config: DownConfig) {
        if (config.url.isEmpty()) throw Throwable("download url is empty")
        // 防止多次下载同一个文件
        if (isDownloading(config)) return
        // 创建retrofit对象
        val builder = OkHttpClient.Builder()
            .addInterceptor(HeadInterceptor(config.headers))
        val retrofit = Retrofit.Builder()
            .client(builder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(UrlUtils.getBaseUrl(config.url))
            .build()
        // 查询记录的下载进度
        val range = DownRecordUtils.getRange(config.url)
        val disposable = retrofit.create(HttpDownService::class.java)
            .download("bytes=$range-", config.url)
            .retryWhen(RetryFunction(config.retry))
            .map {
                FileDownloadUtils.writeCache(it, config, range)
            }
            .bindIOToMainThread()
            .subscribe({
                DownRecordUtils.complete(config.url)
            }, {
                listeners[config]?.onError(it)
                DownRecordUtils.error(config.url)
            }, {
                listeners[config]?.onComplete()
            }, {
                listeners[config]?.onSubscribe(it)
            })
        downloadingDisposables[config] = disposable
    }

    fun pause(config: DownConfig) {
        if (!isDownloading(config)) return
        downloadingDisposables[config]?.dispose()
        listeners[config]?.onPause()
        DownRecordUtils.pause(config.url)
    }

    fun delete(
        config: DownConfig
    ) {
        Observable.just(config)
            .map {
                downloadingDisposables[it]?.dispose()
                FileUtils.delete(it.savePath)
                DownRecordUtils.delete(it.url)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listeners[config]?.onDelete()
            }, {
                listeners[config]?.onError(it)
            })
    }

    fun isDownloading(config: DownConfig): Boolean {
        return downloadingDisposables[config] != null
                && downloadingDisposables[config]?.isDisposed == false
                && DownRecordUtils.isDownloading(config)
    }

    fun getListener(config: DownConfig): HttpDownListener? {
        return listeners[config]
    }

    fun bindListener(config: DownConfig, listener: HttpDownListener) {
        if (config.url.isEmpty()) throw Throwable("download url is empty")
        listeners[config] = listener
    }

    fun unbindListener(config: DownConfig) {
        listeners.remove(config)
    }

    fun isCompleted(config: DownConfig): Boolean {
        return DownRecordUtils.isCompleted(config)
    }

    fun isPause(config: DownConfig): Boolean {
        return DownRecordUtils.isPause(config)
    }

    fun isError(config: DownConfig): Boolean {
        return DownRecordUtils.isError(config)
    }

    fun getProgress(config: DownConfig): DownloadProgress {
        return DownRecordUtils.getProgress(config)
    }

}