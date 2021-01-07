package com.base.library.rxRetrofit

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.listener.HttpListener
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture


/**
 * Description:
 * 单个 API 测试用例
 *
 * @author Alpinist Wang
* Date:    2019-04-24
 */
@RunWith(AndroidJUnit4::class)
class HttpManagerTest {

    private val httpManager = HttpManager()
    private val homeArticlesApi = HomeArticlesApi()

    @Test
    fun request() {
        val future = CompletableFuture<String>()
        httpManager.request(homeArticlesApi, object : HttpListener() {
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

    @Before
    fun init() {
        RxRetrofitApp.application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
    }
}