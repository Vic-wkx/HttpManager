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
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Description:
 * api基类，Http请求参数统一在api中配置
 *
 * @author  WZG
 * Date:    2019-04-25
 */
abstract class BaseApi {
    @Transient
    val apiConfig = RxRetrofitApp.apiConfig.copy()

    val retrofit: Retrofit
        get() {
            //手动创建一个OkHttpClient并设置超时时间缓存等设置
            val builder = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor {
                    Log.d("RxRetrofit", it)
                })
                .connectTimeout(apiConfig.timeOutConfig.connectionTime, TimeUnit.SECONDS)
                .readTimeout(apiConfig.timeOutConfig.readTime, TimeUnit.SECONDS)
                .writeTimeout(apiConfig.timeOutConfig.writeTime, TimeUnit.SECONDS)
                .addInterceptor(HeadInterceptor(this, apiConfig.headers))
            if (apiConfig.cacheConfig.cache) {
                builder.addNetworkInterceptor(NetCacheInterceptor(apiConfig.cacheConfig.onlineCacheTime))
                    .addInterceptor(OfflineCacheInterceptor(apiConfig.cacheConfig.offlineCacheTime))
                    .cache(
                        Cache(
                            File(RxRetrofitApp.application.externalCacheDir, "httpCache"),
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
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiConfig.baseUrl)
                .build()
        }

    abstract fun getObservable(): Observable<String>

}
