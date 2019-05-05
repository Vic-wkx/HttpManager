package com.base.library.rxRetrofit

import android.net.Uri
import com.base.library.rxRetrofit.api.CategoryApi
import com.base.library.rxRetrofit.api.RandomWallpaperApi
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.httpList.HttpListConfig
import com.base.library.rxRetrofit.http.httpList.HttpListListener
import com.base.library.rxRetrofit.http.listener.HttpListener
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CompletableFuture


/**
 * Description:
 *
 * @author Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-24
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = RoboApp::class)
class HttpManagerTest {

    private val httpManager = HttpManager()
    private val randomWallpaperApi = RandomWallpaperApi()
    private val categoryApi = CategoryApi()

    @Test
    fun request() {
        val future = CompletableFuture<String>()
        httpManager.request(randomWallpaperApi, object : HttpListener() {
            override fun onNext(result: String) {
                println("~~~:$result")
                future.complete("Success")
            }

            override fun onError(error: Throwable) {
                future.complete("$error")
            }
        })
        assertEquals("Success", future.get())
    }

    @Test
    fun requestList() {
        val future = CompletableFuture<String>()
        httpManager.request(apis = arrayOf(randomWallpaperApi, categoryApi),
            config = HttpListConfig(
                showLoading = true,
                loadingCancelable = true,
                order = false
            ),
            listener = object : HttpListListener() {
                override fun onNext(resultMap: HashMap<BaseApi, Any>) {
                    if (resultMap.containsKey(randomWallpaperApi) && resultMap.containsKey(
                            categoryApi
                        )
                    ) {
                        println("~~~list:${resultMap[randomWallpaperApi]}\n${resultMap[categoryApi].toString()}")
                    } else {
                        future.complete("resultMap size error:${resultMap.size}")
                    }
                    future.complete("Error")
                }

                override fun onError(error: Throwable) {
                    future.complete("$error")
                }
            }
        )
        assertEquals("Error", future.get())
    }

    @Test
    fun testMimeType() {
        assertEquals("123.zip", Uri.parse("http://baidu.com/123.zip").lastPathSegment)
        assertEquals("123.zip", Uri.parse("http://baidu.com/123.zip?a=1").lastPathSegment)
        assertEquals("123.zip", Uri.parse("http://baidu.com/123.zip/?a=1").lastPathSegment)
    }

    @Before
    fun init() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }
}