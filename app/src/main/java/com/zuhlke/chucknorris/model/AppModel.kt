package com.zuhlke.chucknorris.model

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import com.zuhlke.chucknorris.ChuckNorrisClient
import com.zuhlke.chucknorris.networking.NetworkResult

class AppModel(val chuckNorrisClient: ChuckNorrisClient) {

    private val behaviorSubject: BehaviorSubject<AppState> = BehaviorSubject.create<AppState>()

    val appState: Observable<AppState> =
        behaviorSubject.startWith(AppState.ShowingRandomQuoteView.Loading())

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
        class Finished : ShowingCategoriesView()
    }
}