package com.example.httpmanager

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.httpList.HttpListConfig
import com.example.httpmanager.commen.httpConfig.MyApiConfig
import com.example.httpmanager.commen.httpConfig.MyDownloadConfig
import com.example.httpmanager.commen.httpConfig.MyHttpListApiConfig
import com.example.httpmanager.commen.httpConfig.ResultConverter

/**
 * Description:
 * app入口
 *
 * @author  Alpinist Wang
 * Date:    2019-04-24
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RxRetrofitApp.apply {
            application = this@MyApplication
            apiConfig = MyApiConfig()
            httpListConfig = MyHttpListApiConfig()
            downConfig = MyDownloadConfig()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}