package com.zuhlke.chucknorris.networking

import io.reactivex.Observable
import okhttp3.Request
import okhttp3.Response

interface HttpClient {
    fun fetch(request: Request): Observable<Response>
}

