package com.zuhlke.chucknorris.randomquote

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.zuhlke.chucknorris.R
import com.zuhlke.chucknorris.components.ProgressBarView
import com.zuhlke.chucknorris.model.AppModel
import io.reactivex.disposables.Disposable

interface AppModelActivity {
    fun model(model: AppModel)
}

class RandomQuoteActivity : AppCompatActivity(), AppModelActivity {

    private lateinit var tvChuckNorrisMessage: TextView
    private lateinit var progressView: ProgressBarView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var disposable: Disposable? = null

    private lateinit var randomQuoteViewModel: RandomQuoteViewModel

    private val quoteObserver = Observer<String?> {
        it?.apply { showRandomQuoteResult(this) } ?: randomQuoteViewModel.refresh()
    }

    private val loadingObserver = Observer<Boolean> {
        it?.apply {
            swipeRefreshLayout.isRefreshing = this
            progressView.visibility = if (this) View.VISIBLE else View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.random_quote_activity)
        tvChuckNorrisMessage = findViewById(R.id.tv_chuck_norris_message)
        progressView = findViewById(R.id.pb_loading_indicator)
        swipeRefreshLayout = findViewById(R.id.sr_refresh)

        swipeRefreshLayout.setOnRefreshListener {
            randomQuoteViewModel.refresh()
        }
        progressView.setLoadingMessage(R.string.loading)
        super.onCreate(savedInstanceState)
    }

    override fun model(model: AppModel) {
        android.util.Log.d("WTF MAN"," (╯°□°）╯︵ ┻━┻")
        randomQuoteViewModel = ViewModelProviders.of(this).get(RandomQuoteViewModel::class.java)
        randomQuoteViewModel.appModel = model
        randomQuoteViewModel.currentQuote.observe(this, quoteObserver)
        randomQuoteViewModel.isLoading.observe(this, loadingObserver)
    }

    private fun showRandomQuoteResult(result: String) {
        tvChuckNorrisMessage.text = result
        //swipeRefreshLayout.isRefreshing = false
    }

    private fun showNetworkError() {
        tvChuckNorrisMessage.text = getString(R.string.error_loading_random_quote)
        progressView.visibility = View.GONE
        //swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

//    override fun onResume() {
//        super.onResume()
//        randomQuoteViewModel.currentQuote.observe(this, quoteObserver)
//        randomQuoteViewModel.isLoading.observe(this, loadingObserver)
//    }
//
//    override fun onPause() {
//        randomQuoteViewModel.currentQuote.removeObserver(quoteObserver)
//        randomQuoteViewModel.isLoading.removeObserver(loadingObserver)
//        super.onPause()
//    }
}
