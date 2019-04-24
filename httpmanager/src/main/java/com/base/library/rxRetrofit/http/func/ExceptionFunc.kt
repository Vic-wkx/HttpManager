package com.base.library.rxRetrofit.http.func

import io.reactivex.Observable
import io.reactivex.functions.Function


/**
 * 异常处理
 * Created by WZG on 2017/3/23.
 */
class ExceptionFunc : Function<Throwable, Observable<String>> {

    override fun apply(throwable: Throwable): Observable<String> {
        return Observable.error<String>(throwable)
    }
}
