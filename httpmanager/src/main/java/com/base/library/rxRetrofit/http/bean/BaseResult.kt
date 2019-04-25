package com.base.library.rxRetrofit.http.bean

/**
 * Description:
 * Http返回数据基类
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class BaseResult {
    // 此变量为0表示请求成功
    var code: Int = 0
    // 请求失败时，此变量携带错误信息
    var msg: String = ""
    // 此变量存储返回的String数据
    var res: String = ""
}
