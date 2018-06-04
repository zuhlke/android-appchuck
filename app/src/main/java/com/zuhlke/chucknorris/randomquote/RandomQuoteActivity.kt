package com.zuhlke.chucknorris.randomquote

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.zuhlke.chucknorris.ActivityCreatedCallback
import com.zuhlke.chucknorris.R
import com.zuhlke.chucknorris.model.AppModel

class RandomQuoteActivity : AppCompatActivity(), RandomQuoteView, ActivityCreatedCallback {

    private lateinit var randomQuotePresenter: RandomQuotePresenter
    private lateinit var tvChuckNorrisMessage: TextView
    private lateinit var progressView: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.random_quote_activity)

        category = intent.getStringExtra("category")

        tvChuckNorrisMessage = findViewById(R.id.tv_chuck_norris_message)
        progressView = findViewById(R.id.pb_random_quote_loading_indicator)
        swipeRefreshLayout = findViewById(R.id.sr_refresh)

        swipeRefreshLayout.setOnRefreshListener {
            randomQuotePresenter.refresh()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(appModel: AppModel) {
        randomQuotePresenter = RandomQuotePresenter(this, appModel, category)
    }

    override fun showLoading() {
        progressView.visibility = View.VISIBLE
        tvChuckNorrisMessage.text = ""
        swipeRefreshLayout.isRefreshing = false
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
        randomQuotePresenter.dispose()
        super.onDestroy()
    }
}
