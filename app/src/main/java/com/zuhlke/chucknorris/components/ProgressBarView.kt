package com.zuhlke.chucknorris.components

import android.content.Context
import android.support.annotation.StringRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import com.zuhlke.chucknorris.R

class ProgressBarView : ConstraintLayout {

    private lateinit var textView: TextView

    constructor(context: Context): super(context) { init(context) }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { init(context) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) { init(context) }

    private fun init(context: Context) {
        inflate(context, R.layout.progress_bar_view, this)
        textView = findViewById(R.id.tv_loading_message)
    }

    fun setLoadingMessage(@StringRes resourceId: Int) {
        textView.text = context.getString(resourceId)
    }

}