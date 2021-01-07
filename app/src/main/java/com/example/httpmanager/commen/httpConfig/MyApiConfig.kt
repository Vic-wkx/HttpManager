package com.example.httpmanager.commen.httpConfig

import com.base.library.rxRetrofit.http.api.ApiConfig
import com.base.library.rxRetrofit.http.converter.IResultConverter

/**
 * Description:
 * 全局BaseApi配置
 * BaseApi中的所有设置都可以在这里全局配置
 *
 * @author  Alpinist Wang
 * Date:    2019-05-04
 */
class MyApiConfig : ApiConfig() {
    override var baseUrl = "https://www.wanandroid.com"
    override var resultConverter: IResultConverter = ResultConverter()
}