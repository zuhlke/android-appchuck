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
                when (appState) {
                    is AppState.ShowingCategoriesView.Loading -> {
                        log.debug("Loading categories")
                        view.showLoading()

                        appModel
                            .chuckNorrisClient
                            .fetchCategories()
                            .subscribe {
                                appModel.updateState(AppState.ShowingCategoriesView.Finished(it))
                            }
                    }
                    is AppState.ShowingCategoriesView.Finished -> {
                        when (appState.quoteCategories) {
                            is NetworkResult.Failure -> {
                                log.debug("Failure: " + appState.quoteCategories.throwable.localizedMessage)
                                view.showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                val quoteCategories = appState.quoteCategories.payload
                                    .filterNot { it == "explicit" }
                                log.debug("Success: $quoteCategories")
                                view.showQuoteCategories(quoteCategories)
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
        disposable.dispose()
    }
}