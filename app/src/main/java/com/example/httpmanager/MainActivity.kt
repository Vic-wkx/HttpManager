package com.example.httpmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.base.library.rxRetrofit.http.HttpManager
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.functions.Consumer

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/3
 */
class MainActivity : RxAppCompatActivity() {

    private val httpManager by lazy { HttpManager(this) }

    private val demoApi by lazy { DemoApi() }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        httpManager.request(demoApi, Consumer {
            showWallpaper(it)
        }, Consumer {
            onError(it)
        })
    }

    private fun showWallpaper(result: String) {
        Log.d("~~~", "result:$result")
    }

    private fun onError(error: Throwable?) {
        Log.d("~~~", "error:$error")
    }
}
