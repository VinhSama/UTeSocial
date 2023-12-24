package com.utesocial.android.feature_create_post.presentation

import androidx.recyclerview.widget.GridLayoutManager

class SpanSizeLookup(
    private val spanPos : Int,
    private val spanCount1: Int,
    private val spanCount2: Int
) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        if(position % spanPos == 0) {
            return spanCount2
        }
        return spanCount1
    }
}