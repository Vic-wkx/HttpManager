package com.base.library.rxRetrofit.http.api

import android.util.Log
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.common.header.HeadInterceptor
import com.base.library.rxRetrofit.common.header.HttpLoggingInterceptor
import com.base.library.rxRetrofit.http.cache.NetCacheInterceptor
import com.base.library.rxRetrofit.http.cache.OfflineCacheInterceptor
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Description:
 * api基类，Http请求参数统一在api中配置
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
abstract class BaseApi {
    // Retrofit网络请求的BaseUrl
    var baseUrl = RxRetrofitApp.apiConfig.baseUrl
    // 是否显示Loading弹窗
    var showLoading = RxRetrofitApp.apiConfig.showLoading
    // Loading弹窗是否可取消
    var loadingCancelable = RxRetrofitApp.apiConfig.loadingCancelable
    // 缓存配置
    var cacheConfig = RxRetrofitApp.apiConfig.cacheConfig
    // 是否忽略BaseResult判断
    var ignoreJudge = RxRetrofitApp.apiConfig.ignoreJudge
    // 重试配置
    var retry = RxRetrofitApp.apiConfig.retry
    // 超时时间配置
    var timeOutConfig = RxRetrofitApp.apiConfig.timeOutConfig
    // Http请求head信息，例如Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
    var headers = RxRetrofitApp.apiConfig.headers

    val retrofit: Retrofit
        get() {
            //手动创建一个OkHttpClient并设置超时时间缓存等设置
            val builder = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        Log.d("RxRetrofit", it)
                    })
                )
                .connectTimeout(timeOutConfig.connectionTime, TimeUnit.SECONDS)
                .readTimeout(timeOutConfig.readTime, TimeUnit.SECONDS)
                .writeTimeout(timeOutConfig.writeTime, TimeUnit.SECONDS)
                .addInterceptor(HeadInterceptor(headers))
            if (cacheConfig.cache) {
                builder.addNetworkInterceptor(
                    NetCacheInterceptor(
                        cacheConfig.onlineCacheTime
                    )
                )
                    .addInterceptor(
                        OfflineCacheInterceptor(
                            cacheConfig.offlineCacheTime
                        )
                    )
                    .cache(
                        Cache(
                            File(RxRetrofitApp.application?.externalCacheDir, "httpCache"),
                            1024 * 1024 * 50
                        )
                    )
            }
            /*创建retrofit对象*/
            return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 将返回的数据转换为String
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }

    abstract fun getObservable(): Observable<String>


}
