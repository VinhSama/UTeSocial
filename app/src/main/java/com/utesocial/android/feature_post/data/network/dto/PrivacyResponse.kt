package com.utesocial.android.feature_post.data.network.dto

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.utesocial.android.feature_post.domain.model.PrivacyMode
import java.util.Date

data class PrivacyResponse(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    val postId: String,
    val privacyMode: Int,
    val updatedAt: Date
) {

    fun toPrivacyMode(): PrivacyMode = PrivacyMode(privacyMode = privacyMode)
}