package com.utesocial.android.core.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.utesocial.android.R

class OverlapAvatarItemDecoration(private val numberLimit: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val overlapLeft = view.resources.getDimension(R.dimen.overlap_avatar_left).toInt()
        when (parent.getChildAdapterPosition(view)) {
            0 -> return
            in 1 until numberLimit -> outRect.set(overlapLeft, 0, 0, 0)
            else -> outRect.set(0, 0, 0, 0)
        }
    }
}