package com.base.library.rxRetrofit

import android.app.Application

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
class RoboApp : Application() {

    override fun onCreate() {
        super.onCreate()
        RxRetrofitApp.init(this)
    }
}