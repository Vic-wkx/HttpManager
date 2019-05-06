package com.example.httpmanager.commen.api

import com.alibaba.fastjson.JSONArray
import com.base.library.rxRetrofit.http.api.BaseApi
import com.example.httpmanager.commen.bean.Wallpaper
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

    /**
     * 将返回结果转换成业务层需要的类型
     */
    fun convert(result: String): Wallpaper {
        val wallpaper = JSONArray.parseObject(result, Wallpaper::class.java)
        return wallpaper
    }
}