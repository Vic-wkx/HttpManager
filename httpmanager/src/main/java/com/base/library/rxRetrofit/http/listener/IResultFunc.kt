package com.base.library.rxRetrofit.http.listener

/**
 * Description:
 * Http返回数据基类
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
interface IResultFunc {
    /**
     * 将返回的结果统一处理
     */
    fun convert(response: String): String
}
