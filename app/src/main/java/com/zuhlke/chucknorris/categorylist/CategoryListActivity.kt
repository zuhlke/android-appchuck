package com.zuhlke.chucknorris.categorylist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.zuhlke.chucknorris.ActivityCreatedCallback
import com.zuhlke.chucknorris.R
import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.model.QuoteCategories

class CategoryListActivity : AppCompatActivity(), CategoryListView, ActivityCreatedCallback {

    private lateinit var categoryListPresenter: CategoryListPresenter
    private lateinit var progressView: ProgressBar
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        progressView = findViewById(R.id.pb_categories_loading_indicator)

        viewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView = findViewById<RecyclerView>(R.id.rv_category_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        errorMessage = findViewById(R.id.tv_categories_error_message)
    }

    override fun onActivityCreated(appModel: AppModel) {
        categoryListPresenter = CategoryListPresenter(this, appModel)
    }

    override fun showLoading() {
        progressView.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun showNetworkError() {
        errorMessage.text = getString(R.string.error_loading_categories)
        errorMessage.visibility = View.VISIBLE
        progressView.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun showQuoteCategories(quoteCategories: QuoteCategories) {
        progressView.visibility = View.GONE
        errorMessage.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = CategoryListAdapter(quoteCategories)
    }

    override fun onDestroy() {
        categoryListPresenter.dispose()
        super.onDestroy()
    }


}
