package com.zuhlke.chucknorris.categorylist

import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.model.AppState
import com.zuhlke.chucknorris.networking.NetworkResult
import com.zuhlke.chucknorris.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class CategoryListPresenter(private val view: CategoryListView,
                            private val appModel: AppModel) {

    private val disposable: Disposable
    private val log = Logger(this.javaClass)

    init {
        disposable = appModel
            .appState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { appState ->
                log.debug("==== AppState: $appState")

                when (appState) {
                    is AppState.ShowingCategoriesView.Loading -> {
                        log.debug("Loading")
                        view.showLoading()

                        appModel
                            .chuckNorrisClient
                            .fetchCategories()
                            .subscribe {
                                when (it) {
                                    is NetworkResult.Success,
                                    is NetworkResult.Failure -> {
                                        appModel.updateState(AppState.ShowingCategoriesView.Finished(it))
                                    }
                                }
                            }
                    }
                    is AppState.ShowingCategoriesView.Finished -> {
                        when (appState.networkResult) {
                            is NetworkResult.Failure -> {
                                log.debug("Finished Failure: " + appState.networkResult.throwable.localizedMessage)
                                view.showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                val quoteCategories = appState.networkResult.payload
                                    .filterNot { it == "explicit" }
                                log.debug("Finished Success: $quoteCategories")
                                view.showQuoteCategories(quoteCategories)
                            }
                            is NetworkResult.RequestInProgress -> {
                                log.debug("Finished RequestInProgress")
                            }
                        }
                    }
                }
            }
    }

    fun onCategoryClicked(category: String) {
        appModel.updateState(AppState.ShowingRandomQuoteView.Loading())
        view.launchRandomQuoteActivityWith(category)
    }

    fun dispose() {
        log.debug("dispose")
        disposable.dispose()
    }
}