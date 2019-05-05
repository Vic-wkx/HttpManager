package com.example.httpmanager.http

import android.os.Bundle
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.example.httpmanager.R
import com.example.httpmanager.commen.api.RandomWallpaperApi
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_http.*

/**
 * Description:
 * 单个Http请求
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-05
 */
class HttpActivity : RxAppCompatActivity() {
    private val httpManager by lazy { HttpManager(this) }
    private val randomWallpaperApi by lazy { RandomWallpaperApi() }
    private val httpListener = object : HttpListener() {

        override fun onNext(result: String) {
            tvResult.text = result
        }

        override fun onError(error: Throwable) {
            tvResult.text = error.message
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http)
        initView()
    }

    private fun initView() {
        btnRequest.setOnClickListener {
            httpManager.request(randomWallpaperApi, httpListener)
        }
    }
}
