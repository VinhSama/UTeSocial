package com.utesocial.android.core.presentation.util

import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.utesocial.android.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@DelicateCoroutinesApi
@BindingAdapter("android:text")
fun showTabBadge(
    textView: TextView,
    numberBadge: Int
) {
    if (numberBadge == 0) {
        if (textView.visibility == VISIBLE) {
            textView.isEnabled = false
            GlobalScope.launch(Dispatchers.IO) {
                delay(textView.context.resources.getInteger(R.integer.duration_200).toLong())
                withContext(Dispatchers.Main) { textView.visibility = GONE }
            }
        }
    } else {
        if (textView.visibility == GONE) {
            textView.visibility = VISIBLE
            textView.text = numberBadge.toString()
            textView.isEnabled = true
        }
    }
}

@BindingAdapter("android:background")
fun notifyUnRead(
    view: View,
    unRead: Boolean
) {
    val typedValue = TypedValue()
    view.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurfaceContainerHigh, typedValue, true)
    view.setBackgroundColor(ContextCompat.getColor(view.context, typedValue.resourceId))
}