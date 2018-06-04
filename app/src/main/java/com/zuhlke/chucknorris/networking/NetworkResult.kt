package com.zuhlke.chucknorris.networking

sealed class NetworkResult<T> {
    class RequestInProgress<T>: NetworkResult<T>()
    class Success<T>(val payload: T): NetworkResult<T>()
    class Failure<T>(val throwable: Throwable): NetworkResult<T>()
}