package com.utesocial.android.core.presentation.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.core.presentation.main.element.MainActivity


var showingDialog: Dialog? = null


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
        if(com.utesocial.android.core.presentation.util.showingDialog?.isShowing == true) {
            com.utesocial.android.core.presentation.util.showingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.dismiss()
                if(com.utesocial.android.core.presentation.util.showingDialog == it) {
                    com.utesocial.android.core.presentation.util.showingDialog = null
                }
            }
        })
        com.utesocial.android.core.presentation.util.showingDialog = it
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
        if(com.utesocial.android.core.presentation.util.showingDialog?.isShowing == true) {
            com.utesocial.android.core.presentation.util.showingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.dismiss()
                if (com.utesocial.android.core.presentation.util.showingDialog === it) {
                    com.utesocial.android.core.presentation.util.showingDialog = null
                }
            }
        })
        com.utesocial.android.core.presentation.util.showingDialog = it
        it.show()
        it
    }
}

fun MainActivity.showUnauthorizedDialog(
    onClickListener: ((dialog: Dialog) -> Unit)? = null,
) {
    return MaterialAlertDialogBuilder(this).apply {
        setTitle(getString(R.string.title_dialog_unauthorized))
        setMessage(getString(R.string.msg_dialog_unauthorized))
        setPositiveButton("OK", null)
        setCancelable(false)
    }.create().let { alertDialog ->
        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                onClickListener?.invoke(alertDialog)
            }
        }
        alertDialog.setCanceledOnTouchOutside(false)
        if(showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                alertDialog.dismiss()
                if (showingDialog === alertDialog) {
                    showingDialog = null
                }
            }
        })
        alertDialog.setOnDismissListener {
            showingDialog = null
        }
        showingDialog = alertDialog
        alertDialog.show()
    }
}

fun Fragment.showDialog(
    title: String? = null,
    message: String? = null,
    textPositive: String? = null,
    positiveListener: (() -> Unit)? = null,
    textNegative: String? = null,
    negativeListener: (() -> Unit)? = null,
    cancelable: Boolean = false,
    canceledOnTouchOutside: Boolean = false
): AlertDialog? {
    return MaterialAlertDialogBuilder(context ?: return null).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(textPositive) { _, _ ->
            positiveListener?.invoke()
        }
        setNegativeButton(textNegative) { _, _ ->
            negativeListener?.invoke()
        }
        setCancelable(cancelable)
    }.create().let { dialog ->
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        if (showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                dialog.dismiss()
                if (showingDialog === dialog) {
                    showingDialog = null
                }
            }
        })
        dialog.setOnDismissListener {
            showingDialog = null
        }
        showingDialog = dialog
        dialog.show()
        dialog
    }
}

fun AppCompatActivity.showDialog(
    title: String? = null,
    message: String? = null,
    textPositive: String? = null,
    positiveListener: (() -> Unit)? = null,
    textNegative: String? = null,
    negativeListener: (() -> Unit)? = null,
    cancelable: Boolean = false,
    canceledOnTouchOutside: Boolean = false
): AlertDialog {
    return MaterialAlertDialogBuilder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(textPositive) { _, _ ->
            positiveListener?.invoke()
        }
        setNegativeButton(textNegative) { _, _ ->
            negativeListener?.invoke()
        }
        setCancelable(cancelable)
    }.create().let { dialog ->
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        if (showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                dialog.dismiss()
                if (showingDialog === dialog) {
                    showingDialog = null
                }
            }
        })
        dialog.setOnDismissListener {
            showingDialog = null
        }
        showingDialog = dialog
        dialog.show()
        dialog
    }
}



fun dismissLoadingDialog() {
    if(showingDialog?.isShowing == true) {
        showingDialog?.dismiss()
    }
}

