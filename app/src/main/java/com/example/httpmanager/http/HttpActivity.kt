package com.example.httpmanager.http

import android.os.Bundle
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.example.httpmanager.R
import com.example.httpmanager.commen.api.HomeArticlesApi
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_http.*

/**
 * Description:
 * 单个Http请求示例
 *
 * @author  Alpinist Wang
 * Date:    2019-05-05
 */
class HttpActivity : RxAppCompatActivity() {
    private val httpManager by lazy { HttpManager(this) }
    private val homeArticlesApi by lazy { HomeArticlesApi() }
    private val listener = object : HttpListener() {

        override fun onNext(result: String) {
            // 对数据的解析或其他处理都在api中进行，此界面只做展示相关处理
            val articles = homeArticlesApi.convert(result)
            tvResult.append("获取文章结果示例：${articles.datas.firstOrNull()?.title}\n\n")
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
            httpManager.request(homeArticlesApi, listener)
        }
    }
}
