package com.zuhlke.chucknorris.randomquote

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.networking.NetworkResult
import io.reactivex.android.schedulers.AndroidSchedulers

class RandomQuoteViewModel : ViewModel() {
    lateinit var appModel: AppModel
    var currentQuote: MutableLiveData<String?> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoading.value = false
        currentQuote.value = null
    }

    fun refresh(category: String) {
        isLoading.value = true

        appModel.chuckNorrisClient
                .fetchRandomQuote(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is NetworkResult.Failure -> {
                            //log.debug("Failure: " + appState.randomChuckNorrisQuote.throwable.localizedMessage)
                            currentQuote.value = "There was an error. Please try again"
                            isLoading.value = false
                        }
                        is NetworkResult.Success -> {
                            //log.debug("Success: " + appState.randomChuckNorrisQuote.payload.value)
                            currentQuote.value = it.payload.value
                            isLoading.value = false
                        }
                    }
                }
    }
}