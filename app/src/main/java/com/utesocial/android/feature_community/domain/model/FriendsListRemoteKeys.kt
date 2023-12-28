package com.utesocial.android.feature_community.domain.model

import androidx.room.PrimaryKey

class FriendsListRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val prevKey: Int,
    val currentPage: Int,
    val nextKey: Int?,
    val createdAt: Long = System.currentTimeMillis()
) {
}