package com.utesocial.android.feature_register.data.network.dto

import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.data.network.dto.UserResponseDto
import com.utesocial.android.feature_register.domain.model.FacultyRes

data class FacultyResponseDto(
    val faculties: List<Faculty>,
    val totalCount: Int
) {

    data class Faculty(
        @SerializedName("_id")
        val id: String,
        val code: String,
        val name: String,
        val createdBy: UserResponseDto,
        val updatedBy: UserResponseDto
    ) {

        fun toFacultyRes(): FacultyRes = FacultyRes(
            id = id,
            code = code,
            name = name
        )
    }

    fun toListFacultyRes(): List<FacultyRes> = faculties.map { it.toFacultyRes() }
}