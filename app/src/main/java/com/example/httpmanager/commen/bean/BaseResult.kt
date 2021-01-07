package com.example.httpmanager.commen.bean

/**
 * Description:
 * Http返回数据基类，需要根据后台的接口返回数据自定义
 *
 * @author  Alpinist Wang
 * Date:    2019-05-04
 */
data class BaseResult(
    // 此变量为0表示请求成功
    val errorCode: Int,
    // 请求失败时，此变量携带错误信息
    val errorMsg: String,
    // 此变量存储返回的业务数据
    val data: String?
)