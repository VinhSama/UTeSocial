package com.utesocial.android.feature_post.data.network.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class PrivacyResponse(
    @SerializedName("_id")
    val postId: String,
    val privacyMode: Int,
    val updatedAt: Date
) : Serializable