package com.base.library.rxRetrofit.subscribers

import android.app.ProgressDialog
import com.base.library.rxRetrofit.api.BaseApi
import com.base.library.rxRetrofit.listener.HttpOnNextListener
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
class ProgressSubscriber(
    private val api: BaseApi,
    private val listener: HttpOnNextListener,
    private val activity: RxAppCompatActivity?,
    private val fragment: RxFragment?
) : Observer<String> {

    companion object {
        // 缓存数据的的文件夹名称
        const val CACHE_DATA_DIR = "gson/"
        // json后缀
        const val JSON_SUFFIX = ".json"
    }

    private lateinit var disposable: Disposable
    private val context by lazy {
        activity ?: fragment?.context ?: throw NullPointerException("fragment or activity is null")
    }
    // 加载框
    private val dialog by lazy { ProgressDialog.show(context, null, "Loading") }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    override fun onSubscribe(disposable: Disposable) {
        this.disposable = disposable
        initLoading()
    }

    /**
     * 初始化加载框
     */
    private fun initLoading() {
        if (!api.showLoading) return
        dialog.setCancelable(api.loadingCancelable)
        if (api.loadingCancelable) dialog.setOnCancelListener { onCancelProgress(disposable) }
        dialog.show()
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    override fun onNext(t: String) {
        if (isFinishing()) return
        listener.onNext(t, api.method)
    }

    override fun onComplete() {
        if (isFinishing()) return
        dialog.dismiss()
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    override fun onError(e: Throwable) {
        if (isFinishing()) return
        listener.onError(e, api.method)
        dialog.dismiss()
    }


    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    private fun onCancelProgress(disposable: Disposable?) {
        disposable?.dispose()
        if (isFinishing()) return
        listener.onError(Exception("request cancelled"), api.method)
    }

    /**
     * 判断fragment是否被销毁/activity是否正在关闭
     */
    private fun isFinishing() = (fragment == null && activity == null)
            || (fragment != null && !fragment.isAdded && fragment.activity != null && fragment.activity?.isFinishing == false)
            || (activity != null && activity.isFinishing)

}