package com.zuhlke.chucknorris

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.zuhlke.chucknorris.model.AppModel
import com.zuhlke.chucknorris.networking.AppHttpClient
import com.zuhlke.chucknorris.networking.ChuckNorrisClient
import okhttp3.OkHttpClient

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val httpClient = AppHttpClient(OkHttpClient())
//        val httpClient = MockHttpClient()
        val chuckNorrisClient = ChuckNorrisClient(httpClient)
        val model = AppModel(chuckNorrisClient)

        registerActivityLifecycleCallbacks(ActivityCreationHandler { activity ->
            when (activity) {
                is ActivityCreatedCallback -> activity.onActivityCreated(model)
                else -> throw IllegalStateException("Not handling activity of type: ${activity.javaClass.simpleName}")
            }
        })
    }

    private class ActivityCreationHandler(val onActivityCreated: (Activity) -> Unit)
        : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {}
        override fun onActivityResumed(activity: Activity?) {}
        override fun onActivityStarted(activity: Activity?) {}
        override fun onActivityDestroyed(activity: Activity?) {}
        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
        override fun onActivityStopped(activity: Activity?) {}
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let {
                onActivityCreated(it)
            }
        }
    }
}

