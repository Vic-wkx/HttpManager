package com.base.library.rxRetrofit

import android.app.Application
import com.base.library.rxRetrofit.common.header.DefaultHttpResponseProcessor
import com.base.library.rxRetrofit.common.header.IHttpResponseProcessor
import com.base.library.rxRetrofit.download.config.DefaultDownConfig
import com.base.library.rxRetrofit.http.api.DefaultApiConfig
import com.base.library.rxRetrofit.http.converter.DefaultResultConverter
import com.base.library.rxRetrofit.http.converter.IResultConverter
import com.base.library.rxRetrofit.http.httpList.DefaultHttpListConfig

/**
 * Description:
 * 全局application
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
object RxRetrofitApp {
    /**全局Application*/
    @JvmStatic
    lateinit var application: Application
    /**全局BaseApi配置*/
    @JvmStatic
    var apiConfig: DefaultApiConfig = DefaultApiConfig()
    /**全局HttpList配置*/
    @JvmStatic
    var httpListConfig: DefaultHttpListConfig = DefaultHttpListConfig()
    /**全局DownConfig配置*/
    @JvmStatic
    var downConfig: DefaultDownConfig = DefaultDownConfig()
    /**返回数据统一转换类*/
    @JvmStatic
    var resultConverter: IResultConverter = DefaultResultConverter()
    /**Http响应统一处理类*/
    @JvmStatic
    var httpResponseProcessor: IHttpResponseProcessor = DefaultHttpResponseProcessor()
}
