package com.zuhlke.chucknorris.randomquote

import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.model.AppState
import com.zuhlke.chucknorris.networking.NetworkResult
import com.zuhlke.chucknorris.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers

class RandomQuotePresenter(view: RandomQuoteView,
                           private val appModel: AppModel) {

    private val log = Logger(this.javaClass)

    init {
        appModel
            .appState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { appState ->
                when (appState) {
                    is AppState.ShowingRandomQuoteView.Loading -> {
                        view.showLoading()
                        appModel
                            .chuckNorrisClient
                            .fetchRandomQuote()
                            .subscribe {
                                appModel.sendState(AppState.ShowingRandomQuoteView.Finished(it))
                            }
                    }
                    is AppState.ShowingRandomQuoteView.Finished -> {
                        when (appState.randomChuckNorrisQuote) {
                            is NetworkResult.Failure -> {
                                log.debug("Failure: " + appState.randomChuckNorrisQuote.throwable.localizedMessage)
                                view.showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                log.debug("Success: " + appState.randomChuckNorrisQuote.payload.value)
                                view.showRandomQuoteResult(appState.randomChuckNorrisQuote.payload.value)
                            }
                        }
                    }
                }
            }
    }

    fun refresh() {
        appModel.sendState(AppState.ShowingRandomQuoteView.Loading())
    }

}