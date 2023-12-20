package com.utesocial.android.feature_register.data.repository

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_register.data.network.RegisterApi
import com.utesocial.android.feature_register.data.network.dto.EnrollmentYearResponseDto
import com.utesocial.android.feature_register.data.network.dto.FacultyResponseDto
import com.utesocial.android.feature_register.data.network.dto.MajorResponseDto
import com.utesocial.android.feature_register.data.network.dto.RegisterResponseDto
import com.utesocial.android.feature_register.domain.model.RegisterReq
import com.utesocial.android.feature_register.domain.repository.RegisterRepository

class RegisterRepositoryImpl(private val registerApi: RegisterApi) : RegisterRepository {

    override suspend fun getFaculties(): ResponseDto<FacultyResponseDto> = registerApi.getFaculties()

    override suspend fun getMajorsByFaculty(facultyId: String): ResponseDto<MajorResponseDto> = registerApi.getMajorsByFaculty(facultyId)

    override suspend fun getMajorsByNumberItem(limit: Int): ResponseDto<MajorResponseDto> = registerApi.getMajorByNumberItem(limit)

    override suspend fun getEnrollmentYears(): ResponseDto<EnrollmentYearResponseDto> = registerApi.getEnrollmentYears()

    override suspend fun registerUser(registerReq: RegisterReq): ResponseDto<RegisterResponseDto> = registerApi.registerUser(registerReq)
}