package com.zuhlke.chucknorris.extensions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Response

fun <T> Response.readBodyAsString(clazz: Class<T>): T {
    return jacksonObjectMapper().readValue(
        body()?.string() ?: throw IllegalStateException("Response has got empty body"), clazz)
}
