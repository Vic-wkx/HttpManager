package com.example.httpmanager

import android.app.Application
import com.base.library.rxRetrofit.RxRetrofitApp

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
        RxRetrofitApp.init(this)
    }
}