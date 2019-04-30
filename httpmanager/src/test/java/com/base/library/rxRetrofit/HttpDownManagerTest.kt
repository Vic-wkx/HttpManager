package com.base.library.rxRetrofit

import com.alibaba.fastjson.JSONObject
import com.base.library.rxRetrofit.http.down.DownConfig
import com.base.library.rxRetrofit.http.down.HttpDownManager
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
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-26
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = RoboApp::class)
class HttpDownManagerTest {
    @Test
    fun download() {
        val config = DownConfig().apply {
            url = "http://www.everyayah.com/data/Ghamadi_40kbps/000_versebyverse.zip"
        }
        println("~~~${JSONObject.toJSON(config)}")
        val future = CompletableFuture<String>()
        HttpDownManager.down(config)
        assertEquals("Success", future.get())
    }

    @Before
    fun init() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }
}