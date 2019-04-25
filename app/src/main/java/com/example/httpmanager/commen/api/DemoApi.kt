package com.example.httpmanager.commen.api

import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.Observable

/**
 * Description:
 * api 示例
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class DemoApi : BaseApi() {

    init {
        retry.count = 20
    }
    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(DemoApiService::class.java)
        return apiService.getData()
    }
}