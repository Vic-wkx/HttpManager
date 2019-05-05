package com.example.httpmanager

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.base.library.rxRetrofit.RxRetrofitApp
import com.example.httpmanager.commen.httpConfig.ApiConfig
import com.example.httpmanager.commen.httpConfig.ResultConverter

/**
 * Description:
 * app入口
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-24
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RxRetrofitApp.apply {
            application = this@MyApplication
            resultConverter = ResultConverter()
            apiConfig = ApiConfig()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}