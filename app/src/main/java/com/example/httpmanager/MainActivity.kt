package com.example.httpmanager

import android.os.Bundle
import android.util.Log
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.listener.HttpListListener
import com.base.library.rxRetrofit.http.listener.HttpListener
import com.example.httpmanager.commen.api.CategoryApi
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
    private val categoryApi by lazy { CategoryApi() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRequest.setOnClickListener {
            request()
        }
        btnRequestList.setOnClickListener {
            requestList()
        }
    }

    private fun requestList() {
        httpManager.request(arrayOf(demoApi, categoryApi), object : HttpListListener() {

            override fun onSingleNext(api: BaseApi, result: String): Any {
                when (api) {
                    demoApi -> Log.d("~~~", "demoApi:$result")
                    categoryApi -> Log.d("~~~", "categoryApi:$result")
                    else -> Log.d("~~~", "unknown:$result")
                }
                return super.onSingleNext(api, result)
            }

            override fun onNext(resultMap: HashMap<BaseApi, Any>) {
                if (resultMap.containsKey(demoApi) && resultMap.containsKey(categoryApi)) {
                    Log.d("~~~", "${resultMap[demoApi].toString().substring(0, 10)},${resultMap[categoryApi].toString().substring(0, 10)}")
                } else {
                    Log.d("~~~", "${resultMap.size}")
                }
            }

            override fun onError(error: Throwable, errorMap: HashMap<BaseApi, Throwable>) {
                Log.e("~~~", error.toString())
            }
        })
    }

    private fun request() {
        httpManager.request(categoryApi, object : HttpListener() {

            override fun onNext(result: String) {
                Log.d("~~~", result.substring(0, 10))
            }

            override fun onError(error: Throwable) {
                Log.e("~~~", error.toString())
            }
        })
    }
}
