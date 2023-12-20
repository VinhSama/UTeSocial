package com.utesocial.android.feature_register.data.network.dto

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import com.utesocial.android.core.data.network.dto.UserResponseDto
import com.utesocial.android.feature_register.domain.model.MajorRes

data class MajorResponseDto(
    val majors: List<Major>,
    val totalCount: Int
) {

    data class Major(
        @SerializedName("_id")
        val id: String,
        val code: String,
        val name: LinkedTreeMap<String, String>,
        val faculty: Faculty,
        val createdBy: UserResponseDto,
        val updatedBy: UserResponseDto
    ) {

        fun toMajorRes(): MajorRes = MajorRes(
            id = id,
            code = code,
            name = name
        )
    }

    data class Faculty(
        @SerializedName("_id")
        val id: String,
        val code: String,
        val name: String
    )

    fun toListMajorRes(): List<MajorRes> = majors.map { it.toMajorRes() }
}