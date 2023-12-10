package com.utesocial.android.core.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import androidx.core.widget.NestedScrollView

class PlaceholderLayout : NestedScrollView {

    constructor(context: Context): super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ): super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean = if (motionEvent.action != ACTION_DOWN) {
        super.onTouchEvent(motionEvent)
    } else {
        false
    }
}