package com.zuhlke.chucknorris.networking

import io.reactivex.Observable
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

class MockHttpClient : HttpClient {

    override fun fetch(request: Request): Observable<Response> =
        when (request.url().toString()) {
            "https://api.chucknorris.io/jokes/random" -> {
                Observable.just(
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_0)
                        .code(200)
                        .message("")
                        .body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                randomQuote()
                            )
                        )
                        .build()
                )
                    .delay(500, TimeUnit.MILLISECONDS)
            }
            else -> {
                Observable.just(
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_0)
                        .code(500)
                        .message("")
                        .body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                """ "{ "error": "backend error" } """
                            )
                        )
                        .build()
                )
                    .delay(500, TimeUnit.MILLISECONDS)
            }
        }

    private companion object {
        private val random = Random()

        private val quotes = listOf(
            """ {
                "icon_url" : "",
                "id": "",
                "category": [""],
                "value": "quote 1",
                "url": ""
            } """,
            """ {
                "icon_url" : "",
                "id": "",
                "category": [""],
                "value": "quote 2",
                "url": ""
            } """)

        private fun randomQuote() = quotes[random.nextInt(quotes.size)]
    }

}