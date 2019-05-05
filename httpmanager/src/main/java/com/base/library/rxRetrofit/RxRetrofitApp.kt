package com.base.library.rxRetrofit

import android.app.Application
import com.base.library.rxRetrofit.common.header.DefaultHttpResponseProcessor
import com.base.library.rxRetrofit.common.header.IHttpResponseProcessor
import com.base.library.rxRetrofit.download.config.DefaultDownConfig
import com.base.library.rxRetrofit.http.api.DefaultApiConfig
import com.base.library.rxRetrofit.http.converter.DefaultResultConverter
import com.base.library.rxRetrofit.http.converter.IResultConverter

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
    var application: Application? = null
        private set
    /**返回数据统一处理*/
    @JvmStatic
    lateinit var resultConverter: IResultConverter
        private set
    /**全局BaseApi配置*/
    @JvmStatic
    lateinit var apiConfig: DefaultApiConfig
        private set
    /**Http响应统一处理*/
    @JvmStatic
    lateinit var httpResponseProcessor: IHttpResponseProcessor
        private set
    /**全局DownConfig配置*/
    @JvmStatic
    lateinit var downConfig: DefaultDownConfig
        private set

    /**
     * 初始化RxRetrofit库
     * @param application 全局Application
     * @param resultConverter 返回数据统一转换
     * @param apiConfig 全局修改BaseApi配置
     * @param httpResponseProcessor Http响应统一处理
     * @param downConfig 全局修改DownConfig配置
     */
    fun init(
        application: Application,
        resultConverter: IResultConverter = DefaultResultConverter(),
        apiConfig: DefaultApiConfig = DefaultApiConfig(),
        httpResponseProcessor: IHttpResponseProcessor = DefaultHttpResponseProcessor(),
        downConfig: DefaultDownConfig = DefaultDownConfig()
    ) {
        this.application = application
        this.resultConverter = resultConverter
        this.apiConfig = apiConfig
        this.httpResponseProcessor = httpResponseProcessor
        this.downConfig = downConfig
    }
}
