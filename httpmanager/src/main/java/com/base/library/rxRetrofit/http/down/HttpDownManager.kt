package com.base.library.rxRetrofit.http.down

import android.annotation.SuppressLint
import com.base.library.rxRetrofit.http.converter.RetrofitStringConverterFactory
import com.base.library.rxRetrofit.http.func.RetryFunc
import com.base.library.rxRetrofit.http.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Description:
 * Http下载管理类
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
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(RetrofitStringConverterFactory.create())
            .baseUrl(UrlUtils.getBaseUrl(config.url))
            .build()
        // 查询记录的下载进度
        val range = DownRecordUtils.getRange(config.url)
        val disposable = retrofit.create(HttpDownService::class.java)
            .download("bytes=$range-", config.url)
            .retryWhen(RetryFunc(config.retry))
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