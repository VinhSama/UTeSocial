package com.utesocial.android.feature_post.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommentRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val commentId: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val createdAt: Long = System.currentTimeMillis()
) {
}