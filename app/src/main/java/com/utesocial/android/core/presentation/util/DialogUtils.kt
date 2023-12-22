package com.utesocial.android.core.presentation.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R


var loadingDialog : Dialog? = null

fun Fragment.showLoadingDialog(
    cancelable: Boolean = false,
    canceledOnTouchOutside : Boolean = false
) : AlertDialog? {
    return MaterialAlertDialogBuilder(context ?: return null).apply {
        setView(R.layout.layout_loading_dialog)
    }.create().let {
        it.setCancelable(cancelable)
        it.setCanceledOnTouchOutside(canceledOnTouchOutside)
        it.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.gravity = Gravity.CENTER
        }
        if(loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.dismiss()
                if(loadingDialog == it) {
                    loadingDialog = null
                }
            }
        })
        loadingDialog = it
        it.show()
        it
    }
}

fun AppCompatActivity.showLoadingDialog(
    cancelable: Boolean = false,
    canceledOnTouchOutside : Boolean = false
) : AlertDialog {
    return MaterialAlertDialogBuilder(this).apply {
        setView(R.layout.layout_loading_dialog)
    }.create().let {
        it.setCancelable(cancelable)
        it.setCanceledOnTouchOutside(canceledOnTouchOutside)
        it.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.gravity = Gravity.CENTER
        }
        if(loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.dismiss()
                if (loadingDialog === it) {
                    loadingDialog = null
                }
            }
        })
        loadingDialog = it
        it.show()
        it
    }
}

fun dismissLoadingDialog() {
    if(loadingDialog?.isShowing == true) {
        loadingDialog?.dismiss()
    }
}

