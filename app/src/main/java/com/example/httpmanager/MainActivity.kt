package com.example.httpmanager

import android.content.Intent
import android.os.Bundle
import com.example.httpmanager.download.DownloadActivity
import com.example.httpmanager.http.HttpActivity
import com.example.httpmanager.httpList.HttpListActivity
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Description:
 * 主页面
 *
 * @author  Alpinist Wang
 * Date:    2019/4/3
 */
class MainActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btnHttp.setOnClickListener {
            startActivity(Intent(this@MainActivity, HttpActivity::class.java))
        }
        btnHttpList.setOnClickListener {
            startActivity(Intent(this@MainActivity, HttpListActivity::class.java))
        }
        btnDownload.setOnClickListener {
            startActivity(Intent(this@MainActivity, DownloadActivity::class.java))
        }
    }
}
