package com.zuhlke.chucknorris.categorylist

import com.zuhlke.chucknorris.model.QuoteCategories

interface CategoryListView {
    fun showLoading()
    fun showNetworkError()
    fun showQuoteCategories(quoteCategories: QuoteCategories)
    fun launchRandomQuoteActivityWith(category: String)
}