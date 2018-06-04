package com.zuhlke.chucknorris.model

import com.zuhlke.chucknorris.networking.ChuckNorrisClient
import com.zuhlke.chucknorris.networking.NetworkResult
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AppModel(val chuckNorrisClient: ChuckNorrisClient) {

    private val behaviorSubject: BehaviorSubject<AppState> = BehaviorSubject.create<AppState>()

    val appState: Observable<AppState> =
        behaviorSubject.startWith(AppState.ShowingCategoriesView.Loading())

    fun sendState(newState: AppState) {
        behaviorSubject.onNext(newState)
    }

}

sealed class AppState {

    sealed class ShowingRandomQuoteView : AppState() {
        class Loading : ShowingRandomQuoteView()
        class Finished(val randomChuckNorrisQuote: NetworkResult<RandomChuckNorrisQuote>) : ShowingRandomQuoteView()
    }

    sealed class ShowingCategoriesView : AppState() {
        class Loading : ShowingCategoriesView()
        class Finished(val quoteCategories: NetworkResult<QuoteCategories>)  : ShowingCategoriesView()
    }
}