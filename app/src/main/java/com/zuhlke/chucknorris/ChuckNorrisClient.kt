package com.zuhlke.chucknorris

import com.zuhlke.chucknorris.extensions.readBodyAsString
import com.zuhlke.chucknorris.model.RandomChuckNorrisQuote
import com.zuhlke.chucknorris.networking.HttpClient
import com.zuhlke.chucknorris.networking.NetworkResult
import io.reactivex.Observable
import okhttp3.Request
import java.util.concurrent.TimeUnit

class ChuckNorrisClient(private val httpClient: HttpClient) {

    companion object {
        private const val API_URL = "https://api.chucknorris.io/"
        private const val RANDOM_JOKE_PATH = "jokes/random"
    }

    fun fetchRandomQuote(): Observable<NetworkResult<RandomChuckNorrisQuote>> =
        httpClient
            .fetch(Request.Builder().get().url("$API_URL$RANDOM_JOKE_PATH").build())
            .timeout(1000, TimeUnit.MILLISECONDS)
            .map({
                when (it.code()) {
                    200 -> NetworkResult.Success(it.readBodyAsString(RandomChuckNorrisQuote::class.java))
                    else -> NetworkResult.Failure<RandomChuckNorrisQuote>(IllegalStateException("Unexpected response status code: ${it.code()}"))
                }
            })
            .onErrorReturn { it -> NetworkResult.Failure(it) }
            .startWith(NetworkResult.RequestInProgress())
}