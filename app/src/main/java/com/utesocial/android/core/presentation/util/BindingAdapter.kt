package com.utesocial.android.core.presentation.util

import android.graphics.Color
import android.util.TypedValue
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.utesocial.android.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@BindingAdapter("android:background")
fun notifyUnRead(
    constraintLayout: ConstraintLayout,
    unRead: Boolean
) {
    val hexColor = if (unRead) {
        val context = constraintLayout.context
        val typedValue = TypedValue()

        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        val color = ContextCompat.getColor(context, typedValue.resourceId)
        "#25" + Integer.toHexString(color).substring(2)
    } else {
        "#00000000"
    }
    constraintLayout.setBackgroundColor(Color.parseColor(hexColor))
}

@DelicateCoroutinesApi
@BindingAdapter("android:text")
fun changeTextBadge(
    textView: TextView,
    numberBadge: Int
) {
    val duration = textView.context.resources.getInteger(R.integer.duration_200).toLong()
    if (numberBadge == 0) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(duration)
            withContext(Dispatchers.Main) { textView.visibility = GONE }
        }
    } else if (textView.visibility == GONE) {
        textView.text = numberBadge.toString()
        textView.visibility = VISIBLE
    } else {
        textView.isEnabled = false
        GlobalScope.launch(Dispatchers.IO) {
            delay(duration)
            withContext(Dispatchers.Main) {
                textView.text = numberBadge.toString()
                textView.isEnabled = true
            }
        }
    }
}

@DelicateCoroutinesApi
@BindingAdapter("android:visibility")
fun showContainerBadge(
    frameLayout: FrameLayout,
    isShow: Boolean
) {
    if (isShow) {
        if (frameLayout.visibility == GONE) {
            frameLayout.visibility = VISIBLE
            frameLayout.isEnabled = true
        }
    } else {
        if (frameLayout.visibility == VISIBLE) {
            frameLayout.isEnabled = false
            GlobalScope.launch(Dispatchers.IO) {
                delay(frameLayout.context.resources.getInteger(R.integer.duration_200).toLong())
                withContext(Dispatchers.Main) { frameLayout.visibility = GONE }
            }
        }
    }
}