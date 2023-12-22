package com.utesocial.android.core.data.util

import android.util.Log

object Debug {
    fun log(tag: String?, message: String) {
        val formattedTag = tag ?: ""
        Log.d("UTeSocial", "$tag:$message")
    }

    fun log(message: String) {
        Log.d("UTeSocial", message)
    }

    fun info(tag: String?, message: String) {
        val formattedTag = tag ?: ""
        Log.i("UTeSocial", "$tag:$message")
    }

    fun info(message: String) {
        Log.i("UTeSocial", message)
    }

}