package com.base.library.rxRetrofit.common.retry

import android.util.Log
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit


/**
 * Description:
 * 重试逻辑
 *
 * @author  WZG
 * Date:    2019-04-25
 */
class RetryFunction(private val retry: RetryConfig) :
    Function<Observable<Throwable>, Observable<*>> {
    // 已经重试的次数
    private var count = 0

    override fun apply(observable: Observable<Throwable>): Observable<*> {
        return observable.flatMap {
            if (++count > retry.count) {
                Log.d("RxRetrofit", "Retry finish, cannot get data")
                return@flatMap Observable.error<Throwable>(Throwable("Retry finish, cannot get data. Error is $it"))
            }
            Log.d(
                "RxRetrofit",
                "RetryFunc get error:$it,current count:$count, total retry.count:${retry.count}"
            )
            return@flatMap Observable.timer(
                retry.delay + retry.increaseDelay * count,
                TimeUnit.MILLISECONDS
            )
        }
    }
}
