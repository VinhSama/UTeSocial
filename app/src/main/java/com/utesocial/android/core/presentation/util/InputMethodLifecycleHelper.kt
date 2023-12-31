package com.utesocial.android.core.presentation.util

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class InputMethodLifecycleHelper(
    private var window: Window?,
    private val mode: Mode = Mode.ADJUST_PAN,
    private val root: View
) : DefaultLifecycleObserver {
    private var originalMode: Int = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        setNewSoftInputMode()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        restoreOriginalSoftInputMode()
    }
    private fun setNewSoftInputMode() {
        val originPaddingTop = root.paddingTop
        val originPaddingBottom = root.bottom

        root.setOnApplyWindowInsetsListener { _, insets ->
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                val topStatusBar = insets.getInsets(WindowInsets.Type.statusBars()).top
                root.setPadding(0, topStatusBar + originPaddingTop, 0, imeHeight + originPaddingBottom)
            }
            insets
        }
        window?.let {
            originalMode = it.getSoftInputMode()
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                it.setDecorFitsSystemWindows(mode != Mode.ADJUST_RESIZE)
            } else {
                it.setSoftInputMode(
                    when(mode) {
                        Mode.ADJUST_RESIZE -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        Mode.ADJUST_PAN -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    }
                )
            }
        }
    }

    private fun restoreOriginalSoftInputMode() {
        if (originalMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            window?.let {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                    it.setDecorFitsSystemWindows(originalMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                } else {
                    it.setSoftInputMode(originalMode)
                }
            }
            window?.setSoftInputMode(originalMode)
        }
        window = null
    }
}

fun Window?.getSoftInputMode(): Int {
    return this?.attributes?.softInputMode ?: WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
}

enum class Mode {
    ADJUST_RESIZE, ADJUST_PAN
}