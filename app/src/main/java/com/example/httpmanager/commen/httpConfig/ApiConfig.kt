package com.example.httpmanager.commen.httpConfig

import com.base.library.rxRetrofit.http.config.BaseApiConfig

/**
 * Description:
 * 全局BaseApi配置
 * BaseApi中的所有设置都可以在这里全局配置
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class ApiConfig : BaseApiConfig() {
    override var baseUrl = "http://service.picasso.adesk.com/"
    // 设置全局head信息，例如
    // override var headers = Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
}