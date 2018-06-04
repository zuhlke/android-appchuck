package com.zuhlke.chucknorris.randomquote

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.zuhlke.chucknorris.R
import com.zuhlke.chucknorris.components.ProgressBarView
import com.zuhlke.chucknorris.model.AppModel
import io.reactivex.disposables.Disposable

class RandomQuoteActivity : AppCompatActivity(), RandomQuoteView {

    private lateinit var tvChuckNorrisMessage: TextView
    private lateinit var progressView: ProgressBarView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var disposable: Disposable? = null

    private lateinit var randomQuotePresenter: RandomQuotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.random_quote_activity)
        tvChuckNorrisMessage = findViewById(R.id.tv_chuck_norris_message)
        progressView = findViewById(R.id.pb_loading_indicator)
        swipeRefreshLayout = findViewById(R.id.sr_refresh)

        swipeRefreshLayout.setOnRefreshListener {
            randomQuotePresenter.refresh()
        }
        super.onCreate(savedInstanceState)
    }

    fun onActivityCreated(appModel: AppModel) {
        randomQuotePresenter = RandomQuotePresenter(this, appModel)
    }

    override fun showLoading() {
        progressView.setLoadingMessage(R.string.loading)
        progressView.visibility = View.VISIBLE
    }

    override fun showRandomQuoteResult(result: String) {
        tvChuckNorrisMessage.text = result
        progressView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showNetworkError() {
        tvChuckNorrisMessage.text = getString(R.string.error_loading_random_quote)
        progressView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
