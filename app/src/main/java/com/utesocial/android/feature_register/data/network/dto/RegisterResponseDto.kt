package com.utesocial.android.feature_register.data.network.dto

import com.google.gson.annotations.SerializedName
import com.utesocial.android.feature_register.domain.model.DetailsReq

data class RegisterResponseDto(
    @SerializedName("_id")
    val id: String,
    val identityCode: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val hometown: String,
    val birthdate: String,
    val avatar: Any,
    val status: Int,
    val friends: List<String>,
    val friendCount: Int,
    val details: Any,
    val type: Int,
    val createdAt: Int,
    val updatedAt: Int
)