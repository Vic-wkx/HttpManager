package com.example.httpmanager.commen.httpConfig

import com.base.library.rxRetrofit.http.bean.BaseApiConfig

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
}