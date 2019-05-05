package com.example.httpmanager.commen.httpConfig

import com.alibaba.fastjson.JSONObject
import com.base.library.rxRetrofit.http.converter.IResultConverter
import com.example.httpmanager.commen.bean.BaseResult

/**
 * Description:
 * BaseResult转换
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class ResultConverter : IResultConverter {
    override fun convert(response: String): String {
        // 在这里对结果统一解析
        val result = JSONObject.parseObject(response, BaseResult::class.java)
        // 通过定义的错误码，统一做错误处理
        if (result.code != 0) throw Throwable("code != 0, msg = ${result.msg}")
        return result.res
    }
}