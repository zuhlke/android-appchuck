package com.zuhlke.chucknorris.randomquote

import android.arch.lifecycle.ViewModel
import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.networking.NetworkResult
import io.reactivex.android.schedulers.AndroidSchedulers
import android.arch.lifecycle.MutableLiveData

class RandomQuoteViewModel : ViewModel() {
    lateinit var appModel : AppModel
    var currentQuote : MutableLiveData<String?> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoading.value = false
        currentQuote.value = null
    }

    fun refresh(category: String) {
        isLoading.value = true
            appModel
                    .chuckNorrisClient
                    .fetchRandomQuote(category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        when (it) {
                            is NetworkResult.Failure -> {
                                //log.debug("Failure: " + appState.randomChuckNorrisQuote.throwable.localizedMessage)
                                //showNetworkError()
                            }
                            is NetworkResult.Success -> {
                                //log.debug("Success: " + appState.randomChuckNorrisQuote.payload.value)
                                currentQuote.value = it.payload.value
                            }
                        }
                        isLoading.value = false
                    }
    }
}