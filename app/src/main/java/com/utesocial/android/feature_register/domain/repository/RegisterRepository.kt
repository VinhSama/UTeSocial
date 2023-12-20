package com.utesocial.android.feature_register.domain.repository

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_register.data.network.dto.EnrollmentYearResponseDto
import com.utesocial.android.feature_register.data.network.dto.FacultyResponseDto
import com.utesocial.android.feature_register.data.network.dto.MajorResponseDto
import com.utesocial.android.feature_register.data.network.dto.RegisterResponseDto
import com.utesocial.android.feature_register.domain.model.RegisterReq

interface RegisterRepository {

    suspend fun getFaculties(): ResponseDto<FacultyResponseDto>

    suspend fun getMajorsByFaculty(facultyId: String): ResponseDto<MajorResponseDto>

    suspend fun getMajorsByNumberItem(limit: Int): ResponseDto<MajorResponseDto>

    suspend fun getEnrollmentYears(): ResponseDto<EnrollmentYearResponseDto>

    suspend fun registerUser(registerReq: RegisterReq): ResponseDto<RegisterResponseDto>
}