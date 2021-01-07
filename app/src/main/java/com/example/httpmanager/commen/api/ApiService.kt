package com.example.httpmanager.commen.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Description:
 * 在这里定义所有的接口，与 Retrofit 用法一致
 *
 * @author  Alpinist Wang
 * Date:    2019/4/19
 */
interface ApiService {

    /**
     * 首页文章
     */
    @GET("article/list/{page}/json")
    fun getHomeArticles(@Path("page") page: Int): Observable<String>

    // 首页 banner
    @GET("banner/json")
    fun getBanner(): Observable<String>
}