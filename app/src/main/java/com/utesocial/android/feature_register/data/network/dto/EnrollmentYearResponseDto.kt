package com.utesocial.android.feature_register.data.network.dto

import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.data.network.dto.UserResponseDto
import com.utesocial.android.feature_register.domain.model.EnrollmentYearRes

data class EnrollmentYearResponseDto(
    val enrollmentYears: List<EnrollmentYear>,
    val totalCount: Int
) {

    data class EnrollmentYear(
        @SerializedName("_id")
        val id: String,
        val name: String,
        val startYear: Int,
        val createdBy: UserResponseDto,
        val updatedBy: UserResponseDto
    ) {

        fun toEnrollmentYearRes(): EnrollmentYearRes = EnrollmentYearRes(
            id = id,
            name = name,
            startYear = startYear
        )
    }

    fun toListEnrollmentYear(): List<EnrollmentYearRes> = enrollmentYears.map { it.toEnrollmentYearRes() }
}