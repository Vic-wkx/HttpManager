package com.base.library.rxRetrofit.http.converter

/**
 * Description:
 * 如果没有自定义结果转换器，使用此默认的结果转换器
 *
 * @author  Alpinist Wang
 * Date:    2019-05-04
 */
open class DefaultResultConverter : IResultConverter {
    /**
     * 直接返回原始数据
     */
    override fun convert(response: String) = response
}