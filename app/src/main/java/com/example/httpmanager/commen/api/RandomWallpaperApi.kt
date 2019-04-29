package com.example.httpmanager.commen.api

import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.Observable

/**
 * Description:
 * 不分类获取壁纸api
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class RandomWallpaperApi : BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(WallpaperApiService::class.java)
        return apiService.getRandomWallpaper()
    }
}