package com.base.library.rxRetrofit.common.extension

import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Kotlin对RxJava的扩展
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */

/**
 * 获取activity/fragment生命周期
 */
fun <T> lifeCycle(fragment: RxFragment?, activity: RxAppCompatActivity?): LifecycleTransformer<T>? {
    val fragmentLife = fragment?.bindUntilEvent<T>(FragmentEvent.DESTROY_VIEW)
    val activityLife = activity?.bindUntilEvent<T>(ActivityEvent.DESTROY)
    return fragmentLife ?: activityLife
}

/**
 * 设置订阅在io线程发生，在主线程观察
 */
fun <T> Observable<T>.bindIOToMainThread(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * 绑定生命周期
 */
fun <T> Observable<T>.bindLifeCycle(lifeCycle: LifecycleTransformer<T>?): Observable<T> {
    lifeCycle ?: return this
    /*绑定生命周期*/
    return compose(lifeCycle)
}

