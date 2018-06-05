package com.zuhlke.chucknorris.networking

import com.zuhlke.chucknorris.util.Logger
import io.reactivex.Observable
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

class MockHttpClient : HttpClient {

    private val log = Logger(this.javaClass)

    override fun fetch(request: Request): Observable<Response> {
        val urlString = request.url().toString()
        log.debug("Mock ${request.method()}: $urlString")

        return when {
            urlString.startsWith("https://api.chucknorris.io/jokes/random") -> {
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
                ).delay(500, TimeUnit.MILLISECONDS)
            }
            urlString.startsWith("https://api.chucknorris.io/jokes/categories") -> {
                Observable.just(
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_0)
                        .code(200)
                        .message("")
                        .body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                categories()
                            )
                        )
                        .build()
                ).delay(500, TimeUnit.MILLISECONDS)
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
                ).delay(500, TimeUnit.MILLISECONDS)
            }
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
            } """,
            """ {
                "icon_url" : "",
                "id": "",
                "category": [""],
                "value": "quote 3",
                "url": ""
            } """)

        private fun randomQuote() = quotes[random.nextInt(quotes.size)]
        private fun categories() = """ [ "sports", " music", "cinema" ] """
    }

}