package com.base.library.rxRetrofit.common.utils

/**
 * Description:
 * Url工具类
 *
 * @author  Alpinist Wang
 * Date:    2019-04-27
 */
object UrlUtils {

    /**
     * 从url分割出BaseUrl
     */
    fun getBaseUrl(url: String): String {
        var mutableUrl = url
        var head = ""
        var index = mutableUrl.indexOf("://")
        if (index != -1) {
            head = mutableUrl.substring(0, index + 3)
            mutableUrl = mutableUrl.substring(index + 3)
        }
        index = mutableUrl.indexOf("/")
        if (index != -1) {
            mutableUrl = mutableUrl.substring(0, index + 1)
        }
        return head + mutableUrl
    }
}