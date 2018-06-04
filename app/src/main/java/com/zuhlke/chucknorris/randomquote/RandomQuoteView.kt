package com.zuhlke.chucknorris.randomquote

interface RandomQuoteView {
    fun showLoading()
    fun showRandomQuoteResult(result: String)
    fun showNetworkError()
}