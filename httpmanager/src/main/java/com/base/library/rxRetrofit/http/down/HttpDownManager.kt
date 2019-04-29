package com.base.library.rxRetrofit.http.down

import com.base.library.rxRetrofit.http.converter.RetrofitStringConverterFactory
import com.base.library.rxRetrofit.http.func.RetryFunc
import com.base.library.rxRetrofit.http.utils.FileUtils
import com.base.library.rxRetrofit.http.utils.SPUtils
import com.base.library.rxRetrofit.http.utils.UrlUtils
import com.base.library.rxRetrofit.http.utils.bindIOToMainThread
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
object HttpDownManager {

    private val listeners = HashMap<String, HttpDownListener>()
    private val downloadingDisposables = HashMap<String, Disposable>()

    fun down(config: DownConfig, listener: HttpDownListener) {
        val url = config.url
        if (url.isEmpty()) throw Throwable("download url is empty")
        // 防止多次下载同一个文件
        if (listeners.containsKey(url)) return
        listeners[url] = listener
        // 创建retrofit对象
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(RetrofitStringConverterFactory.create())
            .baseUrl(UrlUtils.getBaseUrl(url))
            .build()
        // 查询记录的下载进度
        val range = SPUtils.getInstance().getLong(url, 0L)
        val disposable = retrofit.create(HttpDownService::class.java)
            .download("bytes=$range-", url)
            .doOnSubscribe {
                // 在map中执行的写入文件操作，所以需要在map之前回调onSubscribe，标志下载开始
                listeners[url]?.onSubscribe(it)
            }
            .retryWhen(RetryFunc(config.retry))
            .map {
                FileUtils.writeCache(it, config, range, listeners[url])
            }
            .bindIOToMainThread()
            .subscribe({

            }, {
                listeners[url]?.onError(it)
                release(url)
            }, {
                SPUtils.getInstance().remove(url)
                listeners[url]?.onComplete()
                release(url)
            }, {})
        downloadingDisposables[url] = disposable
    }

    fun pause(url: String) {
        if (downloadingDisposables[url]?.isDisposed == true) return
        downloadingDisposables[url]?.dispose()
        listeners[url]?.onPause()
        release(url)
    }

    private fun release(url: String) {
        downloadingDisposables.remove(url)
        listeners.remove(url)
    }

    fun isDownloading(url: String): Boolean {
        return downloadingDisposables[url] != null && downloadingDisposables[url]?.isDisposed == false
    }
}