package com.utesocial.android.feature_post.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostsByUserIdRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    var postId: String,
    val userId: String,
    var prevKey: Int?,
    var nextKey: Int?,
    var createdAt: Long = System.currentTimeMillis()
) {
}