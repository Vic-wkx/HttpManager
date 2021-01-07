package com.example.httpmanager.commen.api

import com.alibaba.fastjson.JSONArray
import com.base.library.rxRetrofit.http.api.BaseApi
import com.example.httpmanager.commen.bean.Articles
import io.reactivex.Observable

/**
 * Description:
 * 获取首页文章的 API
 *
 * @author  Alpinist Wang
 * Date:    2019/4/3
 */
class HomeArticlesApi : BaseApi() {
init {
}
    var page = 0

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getHomeArticles(page)
    }

    /**
     * 将返回结果转换成业务层需要的类型
     */
    fun convert(result: String): Articles {
        return JSONArray.parseObject(result, Articles::class.java)
    }
}