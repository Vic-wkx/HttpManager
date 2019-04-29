package com.base.library.rxRetrofit.api

import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.Observable

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
class RandomWallpaperApi: BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(TestApiService::class.java)
        return apiService.getRandomWallpaper()
    }
}