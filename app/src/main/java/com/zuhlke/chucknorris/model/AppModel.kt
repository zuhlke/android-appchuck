package com.zuhlke.chucknorris.model

import com.zuhlke.chucknorris.networking.ChuckNorrisClient
import com.zuhlke.chucknorris.networking.NetworkResult
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AppModel(val chuckNorrisClient: ChuckNorrisClient) {

    private val subject: BehaviorSubject<AppState> = BehaviorSubject
        .createDefault<AppState>(AppState.ShowingCategoriesView.Loading())

    val appState: Observable<AppState> = subject

    fun updateState(newState: AppState) {
        subject.onNext(newState)
    }

}

sealed class AppState {

    sealed class ShowingRandomQuoteView : AppState() {
        class Loading : ShowingRandomQuoteView()
        class Finished(val randomChuckNorrisQuote: NetworkResult<RandomChuckNorrisQuote>) : ShowingRandomQuoteView()
    }

    sealed class ShowingCategoriesView : AppState() {
        class Loading : ShowingCategoriesView()
        class Finished(val networkResult: NetworkResult<QuoteCategories>)  : ShowingCategoriesView()
    }
}