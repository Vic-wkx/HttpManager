package com.base.library.rxRetrofit.http.func


import com.alibaba.fastjson.JSONObject
import com.base.library.rxRetrofit.api.BaseApi
import com.base.library.rxRetrofit.common.bean.BaseResult
import io.reactivex.functions.Function

/**
 * 服务器返回数据判断
 * Created by WZG on 2017/3/23.
 */
class ResultFunc(private val api: BaseApi) : Function<String, String> {

    override fun apply(response: String): String {
        if (response.isEmpty()) throw NullPointerException()
        if (api.ignoreJudge) return response
        /*数据回调格式统一判断*/
        val result = JSONObject.parseObject(response, BaseResult::class.java)
        if (result.code != 0) throw Throwable("code != 0, msg = ${result.msg}")
        return result.res
    }
}
