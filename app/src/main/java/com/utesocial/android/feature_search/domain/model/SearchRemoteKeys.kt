package com.utesocial.android.feature_search.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val createdAt: Long = System.currentTimeMillis()
) {

}