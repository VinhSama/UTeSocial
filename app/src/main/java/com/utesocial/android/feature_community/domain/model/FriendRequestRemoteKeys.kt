package com.utesocial.android.feature_community.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendRequestRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    var requestId: String,
    var prevKey: Int?,
    var nextKey: Int?,
    var createdAt: Long = System.currentTimeMillis()
) {

}