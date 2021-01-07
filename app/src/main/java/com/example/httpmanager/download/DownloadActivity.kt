package com.example.httpmanager.download

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.base.library.rxRetrofit.download.DownloadManager
import com.base.library.rxRetrofit.download.bean.DownloadProgress
import com.base.library.rxRetrofit.download.config.DownloadApi
import com.base.library.rxRetrofit.download.listener.DownloadListener
import com.example.httpmanager.R
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_download.*

/**
 * Description:
 * 下载文件示例
 *
 * @author  Alpinist Wang
 * Date:    2019-05-06
 */
class DownloadActivity : RxAppCompatActivity() {
    private val api = DownloadApi().apply {
        url = "https://media.w3.org/2010/05/sintel/trailer.mp4"
    }

    private val listener = object : DownloadListener() {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            Log.d("~~~", "onSubscribe：开始下载或继续下载")
            tvProgress.text = "开始下载或继续下载"
            showPauseIcon()
        }

        override fun onProgress(downloadProgress: DownloadProgress) {
            tvProgress.text = "下载中: ${downloadProgress.memoryProgress}"
            progressBar.progress = downloadProgress.progress
        }

        override fun onComplete() {
            Log.d("~~~", "onComplete")
            tvProgress.text = "下载完成"
            // 有可能下完时没有达到更新进度要求progressStep，会导致进度条没有走到100%，所以手动设置到100%。
            progressBar.progress = 100
            showDownloadIcon()
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
            tvProgress.text = "已删除"
            progressBar.progress = 0
            showDownloadIcon()
        }

        override fun onError(e: Throwable) {
            Log.d("~~~", "onError:$e")
            tvProgress.text = "下载出错，点击下载按钮继续下载"
            showDownloadIcon()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        // 绑定下载配置与下载监听器
        DownloadManager.bindListener(api, listener)
        initView()
        initDownState()
    }

    private fun initView() {
        tvName.text = api.saveFileName
        tvProgress.text = "尚未开始"
        ivDownload.setOnClickListener {
            when {
                DownloadManager.isDownloading(api) -> DownloadManager.pause(api)
                DownloadManager.isCompleted(api) -> Toast.makeText(
                    this@DownloadActivity,
                    "已经下载完成，如果需要重新下载，请先删除下载文件",
                    Toast.LENGTH_SHORT
                ).show()
                else -> DownloadManager.download(api)
            }
        }
        ivDelete.setOnClickListener {
            DownloadManager.delete(api)
        }
    }

    /**
     * 初始化下载任务的状态
     */
    private fun initDownState() {
        when {
            // 显示正在下载状态
            DownloadManager.isDownloading(api) -> {
                Log.d("~~~", "isDownloading")
                val downloadProgress = DownloadManager.getProgress(api)
                tvProgress.text = "下载中: ${downloadProgress.memoryProgress}"
                progressBar.progress = downloadProgress.progress
                showPauseIcon()
            }
            // 显示已完成状态
            DownloadManager.isCompleted(api) -> {
                tvProgress.text = "下载完成"
                progressBar.progress = 100
                showDownloadIcon()
            }
            // 显示暂停状态
            DownloadManager.isPause(api) -> {
                val downloadProgress = DownloadManager.getProgress(api)
                tvProgress.text = "下载暂停: ${downloadProgress.memoryProgress}"
                progressBar.progress = downloadProgress.progress
                showDownloadIcon()
            }
            // 显示出错状态
            DownloadManager.isError(api) -> {
                val downloadProgress = DownloadManager.getProgress(api)
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
        DownloadManager.unbindListener(api)
    }
}
