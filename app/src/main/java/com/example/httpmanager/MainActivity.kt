package com.example.httpmanager

import android.os.Bundle
import android.util.Log
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.example.httpmanager.commen.api.DemoApi
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Description:
 * 主页面
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class MainActivity : RxAppCompatActivity() {

    private val httpManager by lazy { HttpManager(this) }

    private val demoApi by lazy { DemoApi() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            request()
        }

    }

    private fun request() {
        httpManager.request(demoApi, object : HttpListener() {

            override fun onNext(result: String) {
                Log.d("~~~", result.substring(0, 10))
            }

            override fun onError(error: Throwable) {
                Log.d("~~~", error.toString())
            }
        })
    }
}
