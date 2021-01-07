package com.base.library.rxRetrofit

import com.base.library.rxRetrofit.http.api.BaseApi
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    /**
     * 首页文章
     */
    @GET("article/list/{page}/json")
    fun getHomeArticles(@Path("page") page: Int): Observable<String>
}

class HomeArticlesApi : BaseApi() {
    init {
        apiConfig.baseUrl = "https://www.wanandroid.com"
    }

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getHomeArticles(0)
    }
}