package com.base.library.rxRetrofit.http

import com.base.library.rxRetrofit.api.BaseApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions

/**
 * Description:
 *
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019/4/23
 */
class HttpResult(val api: BaseApi,
                 val myOnNext: Consumer<String>,
                 val myOnError: Consumer<Throwable>,
                 val myOnComplete: Action = Functions.EMPTY_ACTION,
                 val myOnSubscribe: Consumer<Disposable> = Functions.emptyConsumer<Disposable>()
) : Observer<String> {
    override fun onComplete() {
        myOnComplete.run()
    }

    override fun onSubscribe(d: Disposable) {
        myOnSubscribe.accept(d)
    }

    override fun onNext(t: String) {
        myOnNext.accept(t)
    }

    override fun onError(e: Throwable) {
        myOnError.accept(e)
    }
}