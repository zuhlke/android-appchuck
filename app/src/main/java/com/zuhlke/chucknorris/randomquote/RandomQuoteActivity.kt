package com.zuhlke.chucknorris.randomquote

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.zuhlke.chucknorris.R
import com.zuhlke.chucknorris.model.AppModel
import io.reactivex.disposables.Disposable

interface AppModelActivity {
    fun model(model: AppModel)
}

class RandomQuoteActivity : AppCompatActivity(), AppModelActivity {

    private lateinit var tvChuckNorrisMessage: TextView
    private lateinit var progressView: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var category: String
    private var disposable: Disposable? = null

    private val quoteObserver = Observer<String?> {
        it?.apply { showRandomQuoteResult(this) } ?: randomQuoteViewModel().refresh(category)
    }

    private val loadingObserver = Observer<Boolean> {
        it?.apply {
            swipeRefreshLayout.isRefreshing = this
            progressView.visibility = if (this) View.VISIBLE else View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.random_quote_activity)
        category = intent.getStringExtra("category")
        tvChuckNorrisMessage = findViewById(R.id.tv_chuck_norris_message)
        progressView = findViewById(R.id.pb_random_quote_loading_indicator)
        swipeRefreshLayout = findViewById(R.id.sr_refresh)

        swipeRefreshLayout.setOnRefreshListener {
            randomQuoteViewModel().refresh(category)
        }
        super.onCreate(savedInstanceState)
    }

    override fun model(model: AppModel) {
        randomQuoteViewModel().apply {
            appModel = model
        }
    }

    private fun showRandomQuoteResult(result: String) {
        tvChuckNorrisMessage.text = result
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        randomQuoteViewModel().let{ vm ->
            vm.currentQuote.observe(this, quoteObserver)
            vm.isLoading.observe(this, loadingObserver)
        }
    }

    override fun onPause() {
        randomQuoteViewModel().let{ vm ->
            vm.currentQuote.removeObserver( quoteObserver)
            vm.isLoading.removeObserver(loadingObserver)
        }
        super.onPause()
    }

    private fun randomQuoteViewModel() = ViewModelProviders.of(this).get(RandomQuoteViewModel::class.java)
}