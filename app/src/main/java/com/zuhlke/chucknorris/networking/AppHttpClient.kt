package com.zuhlke.chucknorris.networking

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.IOException

class AppHttpClient(private val client: OkHttpClient) : HttpClient {

    override fun fetch(request: Request): Observable<Response> {
        val call = client.newCall(request)

        return Observable
            .create<Response> { source ->
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        source.onError(e ?: IllegalStateException("No Exception present"))
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response != null) {
                            source.onNext(response)
                            source.onComplete()
                        }
                        else source.onError(IllegalStateException("Response is body is invalid"))
                    }
                })
            }
            .subscribeOn(Schedulers.io())
            .doOnDispose(call::cancel)
    }
}