package com.base.library.rxRetrofit.http.func

import com.base.library.rxRetrofit.common.bean.Retry
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit


/**
 * retry条件
 * Created by WZG on 2016/10/17.
 */
class RetryFunc(private val retry: Retry) : Function<Observable<Throwable>, Observable<Throwable>> {

    // 已经重试的次数
    private var count = 0

    override fun apply(observable: Observable<Throwable>): Observable<Throwable> {
        if (count > retry.count) {
            return Observable.error(Exception("Retry finish, cannot get data"))
        }
        return observable.delay(retry.delay + retry.increaseDelay * count++, TimeUnit.MILLISECONDS)
    }
}
