package com.zuhlke.chucknorris.util

import android.util.Log

class Logger(private val clazz: Class<*>) {
    fun debug(message: String) {
        Log.d(clazz.simpleName, message)
    }
}