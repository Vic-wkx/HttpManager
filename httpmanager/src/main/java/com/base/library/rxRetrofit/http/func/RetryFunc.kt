package com.base.library.rxRetrofit.http.func

import android.util.Log
import com.base.library.rxRetrofit.http.config.RetryConfig
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit


/**
 * Description:
 * 重试逻辑
 *
 * @author  WZG
 * Company: Mobile CPX
 * Date:    2019-04-25
 */
class RetryFunc(private val retry: RetryConfig) : Function<Observable<Throwable>, Observable<*>> {
    // 已经重试的次数
    private var count = 0

    override fun apply(observable: Observable<Throwable>): Observable<*> {
        return observable.flatMap {
            if (++count > retry.count) {
                Log.d("~~~", "Retry finish, cannot get data")
                return@flatMap Observable.error<Throwable>(Throwable("Retry finish, cannot get data. Error is $it"))
            }
            Log.d("~~~", "RetryFunc get error:$it,current count:$count, total retry.count:${retry.count}")
            return@flatMap Observable.timer(retry.delay + retry.increaseDelay * count, TimeUnit.MILLISECONDS)
        }
    }

}
