package com.base.library.rxRetrofit.api

import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.common.bean.Retry
import com.base.library.rxRetrofit.http.converter.RetrofitStringConverterFactory
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Describe:请求数据统一封装类
 *
 * Created by zhigang wei
 * on 2017/8/29.
 *
 * Company :Sichuan Ziyan
 */
abstract class BaseApi {
    private val baseUrl = "http://service.picasso.adesk.com/"
    /**Loading弹窗是否可取消*/
    var loadingCancelable = true
    /**是否显示Loading弹窗*/
    var showLoading = true
    /**方法名*/
    var method = ""
    /**是否忽略BaseResult判断*/
    var ignoreJudge: Boolean = false
    /**重试配置*/
    var retry = Retry()
    /**超时时间*/
    var connectionTime = 60L

    val retrofit: Retrofit
        get() {
            //手动创建一个OkHttpClient并设置超时时间缓存等设置
            val builder = OkHttpClient.Builder().apply {
                connectTimeout(connectionTime, TimeUnit.SECONDS)
                cache(Cache(File(RxRetrofitApp.application?.cacheDir, "cache"), 1024 * 1024 * 50))
            }
            /*创建retrofit对象*/
            return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(RetrofitStringConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }

    abstract fun getObservable(): Observable<String>
}
