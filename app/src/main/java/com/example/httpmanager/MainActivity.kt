package com.example.httpmanager

import android.os.Bundle
import android.util.Log
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.down.DownConfig
import com.base.library.rxRetrofit.http.down.HttpDownListener
import com.base.library.rxRetrofit.http.down.HttpDownManager
import com.base.library.rxRetrofit.http.list.HttpListConfig
import com.base.library.rxRetrofit.http.listener.HttpListListener
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.example.httpmanager.commen.api.CategoryApi
import com.example.httpmanager.commen.api.RandomWallpaperApi
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.disposables.Disposable
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
    private val randomWallpaperApi by lazy { RandomWallpaperApi() }
    private val categoryApi by lazy { CategoryApi() }
    private val downUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    private val httpListener = object : HttpListener() {

        override fun onNext(result: String) {
            Log.d("~~~", result)
        }

        override fun onError(error: Throwable) {
            Log.e("~~~", error.toString())
        }
    }

    private val httpListListener = object : HttpListListener() {

        override fun onSingleNext(api: BaseApi, result: String): Any {
            when (api) {
                randomWallpaperApi -> {
                    Log.d("~~~", "randomWallpaperApi:$result")
                    return 123
                }
                categoryApi -> Log.d("~~~", "categoryApi:$result")
                else -> Log.d("~~~", "unknown:$result")
            }
            return super.onSingleNext(api, result)
        }

        override fun onNext(resultMap: HashMap<BaseApi, Any>) {
            if (resultMap.containsKey(randomWallpaperApi) && resultMap.containsKey(categoryApi)) {
                Log.d(
                    "~~~",
                    "${resultMap[randomWallpaperApi] as Int}\n${resultMap[categoryApi].toString()}"
                )
            } else {
                Log.d("~~~", "${resultMap.size}")
            }
        }

        override fun onError(error: Throwable) {
            Log.e("~~~", error.toString())
        }

    }

    private val httpDownListener = object : HttpDownListener() {
        override fun onProgress(read: Long, total: Long) {
            Log.d("~~~", "onProgress:$read / $total = ${read * 100 / total}")
        }

        override fun onComplete() {
            Log.d("~~~", "onComplete")
        }

        override fun onError(e: Throwable) {
            Log.d("~~~", "onError:$e")
        }

        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            Log.d("~~~", "onSubscribe")
        }

        override fun onPause() {
            super.onPause()
            Log.d("~~~", "onPause")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRequest.setOnClickListener {
            httpManager.request(categoryApi, httpListener)
        }
        btnRequestList.setOnClickListener {
            httpManager.request(
                arrayOf(randomWallpaperApi, categoryApi),
                HttpListConfig(showLoading = true, loadingCancelable = true, order = false),
                httpListListener
            )
        }
        btnDownload.setOnClickListener {
            Log.d("~~~", "click")
            val config = DownConfig().apply {
                url = downUrl
            }
            HttpDownManager.down(config, httpDownListener)
        }

        btnPause.setOnClickListener {
            HttpDownManager.pause(downUrl)
        }
    }

}
