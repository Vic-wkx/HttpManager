package com.example.httpmanager.commen.api

import com.alibaba.fastjson.JSONArray
import com.base.library.rxRetrofit.http.api.BaseApi
import com.example.httpmanager.commen.bean.Category
import io.reactivex.Observable

/**
 * Description:
 * 分类获取壁纸Api
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class CategoryApi : BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(WallpaperApiService::class.java)
        return apiService.getCategory()
    }

    /**
     * 将返回结果转换成业务层需要的类型
     */
    fun convert(result: String): Category {
        val category = JSONArray.parseObject(result, Category::class.java)
        return category
    }
}