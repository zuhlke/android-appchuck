package com.zuhlke.chucknorris

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.networking.AppHttpClient
import com.zuhlke.chucknorris.randomquote.AppModelActivity
import com.zuhlke.chucknorris.randomquote.RandomQuoteActivity
import com.zuhlke.chucknorris.randomquote.RandomQuoteView
import com.zuhlke.chucknorris.randomquote.RandomQuoteViewModel
import com.zuhlke.chucknorris.networking.ChuckNorrisClient
import okhttp3.OkHttpClient

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val httpClient = AppHttpClient(OkHttpClient())
//        val httpClient = MockHttpClient()
        val chuckNorrisClient = ChuckNorrisClient(httpClient)
        val model = AppModel(chuckNorrisClient)

        registerActivityLifecycleCallbacks(ActivityLifeCycleCallbackHandler(model))
    }
}

class ActivityLifeCycleCallbackHandler(private val appModel: AppModel) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        when (activity) {
            is AppModelActivity -> activity.model(appModel)
            else -> throw RuntimeException("WTF! (╯°□°）╯︵ ┻━┻")
        }
    }
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityDestroyed(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityStopped(activity: Activity?) {}
}