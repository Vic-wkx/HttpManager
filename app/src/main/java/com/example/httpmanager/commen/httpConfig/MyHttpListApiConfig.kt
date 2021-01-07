package com.example.httpmanager.commen.httpConfig

import com.base.library.rxRetrofit.http.httpList.HttpListConfig

/**
 * Description:
 * 全局多接口请求配置
 * HttpListConfig 中的所有设置都可以在这里全局配置
 *
 * @author  Alpinist Wang
 * Date:    2021-01-07
 */
class MyHttpListApiConfig : HttpListConfig() {
    // 是否按顺序请求，默认 true
    override var order = false
}