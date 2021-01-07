package com.base.library.rxRetrofit.common.extension

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Kotlin对RxJava的扩展
 *
 * @author  Alpinist Wang
 * Date:    2019-04-25
 */

/**
 * 设置订阅在io线程发生，在主线程观察
 */
fun <T> Observable<T>.bindIOToMainThread(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

