package com.base.library.rxRetrofit.http.config

/**
 * Description:
 * 如果未设置BaseResult，使用此默认的ResultFunc
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-04
 */
class DefaultResultFunc : IResultFunc {

    /**
     * 直接返回原始数据
     */
    override fun convert(response: String) = response
}