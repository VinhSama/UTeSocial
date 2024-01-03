package com.utesocial.android.feature_search.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchUserRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    var userId: String,
    var prevKey: Int?,
    var nextKey: Int?,
    var createdAt: Long = System.currentTimeMillis()
)