package com.base.library.rxRetrofit.http.func


import com.alibaba.fastjson.JSONObject
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.bean.BaseResult
import io.reactivex.functions.Function

/**
 * Description:
 * 服务器返回数据解析
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class ResultFunc(private val api: BaseApi) : Function<String, String> {

    override fun apply(response: String): String {
        if (api.ignoreJudge) return response
        if (response.isEmpty()) throw NullPointerException("response is empty.")
        // 根据BaseResult解析数据
        val result = JSONObject.parseObject(response, BaseResult::class.java)
        if (result.code != 0) throw Throwable("code != 0, msg = ${result.msg}")
        return result.res
    }
}
