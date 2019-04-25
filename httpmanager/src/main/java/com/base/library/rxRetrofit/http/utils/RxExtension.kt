package com.base.library.rxRetrofit.http.utils

import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-04-25
 */

/**
 * 将订阅与activity/fragment生命周期绑定
 */
fun <T> lifeCycle(fragment: RxFragment?, activity: RxAppCompatActivity?): LifecycleTransformer<T> {
    val fragmentLife = fragment?.bindUntilEvent<T>(FragmentEvent.DESTROY_VIEW)
    val activityLife = activity?.bindUntilEvent<T>(ActivityEvent.DESTROY)
    return fragmentLife ?: activityLife
    ?: throw Throwable("activity or fragment is null")
}

/**
 * 设置订阅在io线程发生，在主线程观察，并绑定生命周期
 */
fun <T> Observable<T>.bind(fragment: RxFragment?, activity: RxAppCompatActivity?): Observable<T> {
    /*http请求线程*/
    return subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .unsubscribeOn(io.reactivex.schedulers.Schedulers.io())
            /*回调线程*/
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            /*绑定生命周期*/
            .compose(lifeCycle(fragment, activity))
}

