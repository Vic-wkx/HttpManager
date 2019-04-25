package com.base.library.rxRetrofit

import android.app.Application

/**
 * Description:
 * 全局application
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
object RxRetrofitApp {
    @JvmStatic
    var application: Application? = null
        private set

    fun init(app: Application) {
        application = app
    }
}
