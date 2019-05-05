package com.base.library.rxRetrofit.http.observer

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-05
 */
fun runOnUIThread(function: () -> Unit) {
    Observable.just(1)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            function.invoke()
        }
}