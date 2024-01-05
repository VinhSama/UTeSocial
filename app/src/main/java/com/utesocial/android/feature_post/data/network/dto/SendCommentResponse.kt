package com.utesocial.android.feature_post.data.network.dto

import com.utesocial.android.feature_post.domain.model.Comment

data class SendCommentResponse(
    val comment: Comment,
    val commentCounts: Int
) {
    override fun toString(): String {
        return "SendCommentResponse(comment=$comment, commentCounts=$commentCounts)"
    }
}