package com.base.library.rxRetrofit.http.func


import com.base.library.rxRetrofit.RxRetrofitApp
import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.functions.Function

/**
 * Description:
 * 服务器返回数据解析
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class HttpResultFunc(private val api: BaseApi) : Function<String, String> {

    override fun apply(response: String): String {
        if (api.ignoreJudge) return response
        if (response.isEmpty()) throw NullPointerException("response is empty.")
        // 根据BaseResult解析数据
        return RxRetrofitApp.resultFunc.convert(response)
    }
}
