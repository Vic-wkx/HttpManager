package com.base.library.rxRetrofit

import android.app.Application
import com.base.library.rxRetrofit.httpConfig.ApiConfig

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
        RxRetrofitApp.apply {
            application = this@RoboApp
            apiConfig = ApiConfig()
        }
    }
}