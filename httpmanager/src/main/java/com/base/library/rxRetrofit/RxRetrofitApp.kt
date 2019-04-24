package com.base.library.rxRetrofit

import android.app.Application

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */
object RxRetrofitApp {
    @JvmStatic
    var application: Application? = null
        private set

    fun init(app: Application) {
        application = app
    }
}
