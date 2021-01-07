package com.example.httpmanager.commen.api

import com.alibaba.fastjson.JSONArray
import com.base.library.rxRetrofit.http.api.BaseApi
import com.example.httpmanager.commen.bean.BannerBean
import io.reactivex.Observable

/**
 * Description:
 * 获取首页 Banner 的 API
 *
 * @author  Alpinist Wang
 * Date:    2019-04-25
 */
class BannerApi : BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getBanner()
    }

    /**
     * 将返回结果转换成业务层需要的类型
     */
    fun convert(result: String): List<BannerBean> {
        return JSONArray.parseArray(result, BannerBean::class.java)
    }
}