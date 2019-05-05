package com.base.library.rxRetrofit.download.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Description:
 *
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
interface HttpDownService {

    /*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @Streaming
    @GET
    fun download(@Header("Range") range: String, @Url url: String): Observable<ResponseBody>
}