package com.base.library.rxRetrofit.http.converter

/**
 * Description:
 * Http返回数据统一转换
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
interface IResultConverter {
    /**
     * 将返回的结果统一处理
     */
    fun convert(response: String): String
}
