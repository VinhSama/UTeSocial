package com.utesocial.android.core.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OverlapAvatarItemDecoration(
    private val numberLimit: Int,
    private val overlapWidth: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        when (parent.getChildAdapterPosition(view)) {
            0 -> return
            in 1 .. numberLimit -> outRect.set(overlapWidth, 0, 0, 0)
            else -> outRect.set(0, 0, 0, 0)
        }
    }
}