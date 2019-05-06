package com.base.library.rxRetrofit.http.converter

import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.functions.Function

/**
 * Description:
 * 服务器返回数据统一转换
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class HttpResultConverter(private val api: BaseApi) : Function<String, String> {

    override fun apply(response: String): String {
        if (api.ignoreResultConverter) return response
        if (response.isEmpty()) throw NullPointerException("response is empty.")
        // 根据结果转换器解析数据
        return RxRetrofitApp.resultConverter.convert(response)
    }
}
