package com.example.httpmanager.httpList

import android.os.Bundle
import android.util.Log
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.httpList.HttpListConfig
import com.base.library.rxRetrofit.http.httpList.HttpListListener
import com.example.httpmanager.R
import com.example.httpmanager.commen.api.CategoryApi
import com.example.httpmanager.commen.api.RandomWallpaperApi
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_http_list.*

class HttpListActivity : RxAppCompatActivity() {
    private val httpManager by lazy { HttpManager(this) }
    private val randomWallpaperApi by lazy { RandomWallpaperApi() }
    private val categoryApi by lazy { CategoryApi() }
    private val httpListListener = object : HttpListListener() {
        override fun onSingleNext(api: BaseApi, result: String): Any {
            when (api) {
                randomWallpaperApi -> {
                    Log.d("HttpListActivity", "收到单个结果：randomWallpaperApi:$result")
                    // 这里可以将返回的字符串转换为任意对象，一般在这里使用Gson/fastJson解析对象
                    return 123
                }
                categoryApi -> Log.d("HttpListActivity", "收到单个结果：categoryApi:$result")
            }
            return super.onSingleNext(api, result)
        }

        override fun onNext(resultMap: HashMap<BaseApi, Any>) {
            // 通过 as 方法，将resultMap中保存的对象取出并转换成onSingleNext返回的类型
            tvResultList.text = "randomWallpaperApi result: ${resultMap[randomWallpaperApi] as Int}\n" +
                    "categoryApi result: ${resultMap[categoryApi].toString()}"
        }

        override fun onError(error: Throwable) {
            tvResultList.text = error.message
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_list)
        initView()

    }

    private fun initView() {
        btnRequestList.setOnClickListener {
            httpManager.request(
                arrayOf(randomWallpaperApi, categoryApi),
                HttpListConfig(
                    showLoading = true,
                    loadingCancelable = true,
                    order = false
                ),
                httpListListener
            )
        }
    }
}
