package com.utesocial.android.core.data.network.dto

import com.google.gson.annotations.SerializedName

data class AvatarResponseDto(
    @SerializedName("_id")
    val id: String,
    val url: String
)