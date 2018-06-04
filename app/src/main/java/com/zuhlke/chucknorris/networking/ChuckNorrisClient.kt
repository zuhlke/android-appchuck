package com.zuhlke.chucknorris.networking

import com.zuhlke.chucknorris.extensions.readBodyAsString
import com.zuhlke.chucknorris.model.QuoteCategories
import com.zuhlke.chucknorris.model.RandomChuckNorrisQuote
import io.reactivex.Observable
import okhttp3.Request
import java.util.concurrent.TimeUnit

class ChuckNorrisClient(private val httpClient: HttpClient) {

    companion object {
        private const val API_URL = "https://api.chucknorris.io/"
        private const val RANDOM_JOKE_PATH = "jokes/random?category="
        private const val CATEGORIES_JOKE_PATH = "jokes/categories"
    }

    fun fetchRandomQuote(category: String): Observable<NetworkResult<RandomChuckNorrisQuote>> =
        httpClient
            .fetch(Request.Builder().get().url("$API_URL$RANDOM_JOKE_PATH$category").build())
            .timeout(10000, TimeUnit.MILLISECONDS)
            .map({
                when (it.code()) {
                    200 -> NetworkResult.Success(it.readBodyAsString(RandomChuckNorrisQuote::class.java))
                    else -> NetworkResult.Failure<RandomChuckNorrisQuote>(IllegalStateException("Unexpected response status code: ${it.code()}"))
                }
            })
            .onErrorReturn { it -> NetworkResult.Failure(it) }
            .startWith(NetworkResult.RequestInProgress())

    fun fetchCategories(): Observable<NetworkResult<QuoteCategories>> =
        httpClient
            .fetch(Request.Builder().get().url("$API_URL$CATEGORIES_JOKE_PATH").build())
            .timeout(10000, TimeUnit.MILLISECONDS)
            .map({
                when (it.code()) {
                    200 -> {
                        val list = it.readBodyAsString(List::class.java) as QuoteCategories
                        NetworkResult.Success(list)
                    }
                    else -> NetworkResult.Failure<QuoteCategories>(IllegalStateException("Unexpected response status code: ${it.code()}"))
                }
            })
            .onErrorReturn { it -> NetworkResult.Failure(it) }
            .startWith(NetworkResult.RequestInProgress())
}