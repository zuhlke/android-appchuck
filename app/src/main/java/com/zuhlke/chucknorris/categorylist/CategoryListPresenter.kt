package com.zuhlke.chucknorris.categorylist

import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.model.AppState
import com.zuhlke.chucknorris.networking.NetworkResult
import com.zuhlke.chucknorris.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class CategoryListPresenter(view: CategoryListView, appModel: AppModel) {

    private val disposable: Disposable
    private val log = Logger(this.javaClass)

    init {
        disposable =
            appModel
            .appState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { appState ->
                when (appState) {
                    is AppState.ShowingCategoriesView.Loading -> {
                        view.showLoading()
                        appModel
                            .chuckNorrisClient
                            .fetchCategories()
                            .subscribe {
                                appModel.sendState(AppState.ShowingCategoriesView.Finished(it))
                            }
                    }
                    is AppState.ShowingCategoriesView.Finished -> {
                        when (appState.quoteCategories) {
                            is NetworkResult.Failure -> {
                                log.debug("Failure: " + appState.quoteCategories.throwable.localizedMessage)
                                view.showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                log.debug("Success: " + appState.quoteCategories.payload)
                                view.showQuoteCategories(appState.quoteCategories.payload)
                            }
                        }
                    }
                }
            }
    }

    fun dispose() {
        disposable.dispose()
    }
}