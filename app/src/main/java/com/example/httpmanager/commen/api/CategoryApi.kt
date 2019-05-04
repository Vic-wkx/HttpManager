package com.example.httpmanager.commen.api

import com.base.library.rxRetrofit.http.api.BaseApi
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

}