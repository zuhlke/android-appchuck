package com.zuhlke.chucknorris.randomquote

import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.model.AppState
import com.zuhlke.chucknorris.networking.NetworkResult
import com.zuhlke.chucknorris.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class RandomQuotePresenter(view: RandomQuoteView,
                           private val appModel: AppModel,
                           category: String) {

    private val disposable: Disposable

    private val log = Logger(this.javaClass)

    init {
        disposable = appModel
            .appState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { appState ->
                when (appState) {
                    is AppState.ShowingRandomQuoteView.Loading -> {
                        log.debug("Loading quotes for category: $category")
                        view.showLoading()
                        appModel
                            .chuckNorrisClient
                            .fetchRandomQuote(category)
                            .subscribe {
                                appModel.updateState(AppState.ShowingRandomQuoteView.Finished(it))
                            }
                    }
                    is AppState.ShowingRandomQuoteView.Finished -> {
                        when (appState.randomChuckNorrisQuote) {
                            is NetworkResult.Failure -> {
                                log.debug("Failure: " + appState.randomChuckNorrisQuote.throwable.localizedMessage)
                                view.showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                val randomQuote = appState.randomChuckNorrisQuote.payload.value
                                log.debug("Success: $randomQuote")
                                view.showRandomQuoteResult(randomQuote)
                            }
                        }
                    }
                }
            }
    }

    fun refresh() {
        appModel.updateState(AppState.ShowingRandomQuoteView.Loading())
    }

    fun dispose() {
        disposable.dispose()
    }

}