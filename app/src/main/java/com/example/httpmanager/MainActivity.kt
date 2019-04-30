package com.example.httpmanager

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.base.library.rxRetrofit.http.HttpManager
import com.base.library.rxRetrofit.http.api.BaseApi
import com.base.library.rxRetrofit.http.down.DownConfig
import com.base.library.rxRetrofit.http.down.DownloadProgress
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
    private val config = DownConfig().apply {
        url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    }
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
        override fun onProgress(downloadProgress: DownloadProgress) {
            Log.d("~~~", "onProgress:$downloadProgress")
            tvProgress.text = "下载中: ${downloadProgress.memoryProgress}"
            progressBar.progress = downloadProgress.progress
        }

        override fun onComplete() {
            Log.d("~~~", "onComplete")
            tvProgress.text = "下载完成"
            showDownloadIcon()
        }

        override fun onError(e: Throwable) {
            Log.d("~~~", "onError:$e")
            tvProgress.text = "下载出错，点击下载按钮继续下载"
            showDownloadIcon()
        }

        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            Log.d("~~~", "onSubscribe")
            showPauseIcon()
        }

        override fun onPause() {
            super.onPause()
            Log.d("~~~", "onPause")
            tvProgress.text = "下载暂停"
            showDownloadIcon()
        }

        override fun onDelete() {
            super.onDelete()
            Log.d("~~~", "onDelete")
            progressBar.progress = 0
            tvProgress.text = "已删除"
            showDownloadIcon()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        HttpDownManager.bindListener(config, httpDownListener)
        initDownState()
    }

    private fun initView() {
        tvName.text = config.saveFileName
        tvProgress.text = "尚未开始"
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

        ivDownload.setOnClickListener {
            if (HttpDownManager.isDownloading(config)) {
                HttpDownManager.pause(config)
            } else {
                if (HttpDownManager.isCompleted(config)) {
                    Toast.makeText(
                        this@MainActivity,
                        "已经下载完成，如果需要重新下载，请先删除下载文件",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                HttpDownManager.down(config)
            }
        }

        ivDelete.setOnClickListener {
            HttpDownManager.delete(config)
        }
    }

    /**
     * 初始化下载任务的状态
     */
    private fun initDownState() {
        when {
            // 如果正在下载，无需设置tvProgress和progressBar，因为此页面绑定了监听器，收到进度回调时会改变其UI
            HttpDownManager.isDownloading(config) -> {
                Log.d("~~~", "isDownloading")
                showPauseIcon()
            }
            // 显示已完成状态
            HttpDownManager.isCompleted(config) -> {
                tvProgress.text = "下载完成"
                progressBar.progress = 100
                showDownloadIcon()
            }
            // 显示暂停状态
            HttpDownManager.isPause(config) -> {
                val downloadProgress = HttpDownManager.getProgress(config)
                tvProgress.text = "下载暂停: ${downloadProgress.memoryProgress}"
                progressBar.progress = downloadProgress.progress
                showDownloadIcon()
            }
            // 显示出错状态
            HttpDownManager.isError(config) -> {
                val downloadProgress = HttpDownManager.getProgress(config)
                tvProgress.text = "下载出错，点击下载按钮继续下载: ${downloadProgress.memoryProgress}"
                progressBar.progress = downloadProgress.progress
                showDownloadIcon()
            }
        }
    }

    private fun showDownloadIcon() {
        ivDownload.setImageResource(android.R.drawable.stat_sys_download)
    }

    private fun showPauseIcon() {
        ivDownload.setImageResource(android.R.drawable.ic_media_pause)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 关闭页面时，解绑监听器，防止内存泄漏
//        HttpDownManager.unbindListener(config)
    }
}
