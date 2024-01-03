package com.utesocial.android.feature_post.data.network.dto

import com.utesocial.android.feature_post.domain.model.Comment

data class CommentsResponse(
    val comments: List<Comment>,
    val totalCount: Int
) {
}