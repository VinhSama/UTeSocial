package com.utesocial.android.feature_register.data.network

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_register.data.network.dto.EnrollmentYearResponseDto
import com.utesocial.android.feature_register.data.network.dto.FacultyResponseDto
import com.utesocial.android.feature_register.data.network.dto.MajorResponseDto
import com.utesocial.android.feature_register.data.network.dto.RegisterResponseDto
import com.utesocial.android.feature_register.domain.model.RegisterReq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RegisterApi {

    @GET("/api/v1/faculty")
    suspend fun getFaculties(): ResponseDto<FacultyResponseDto>

    @GET("/api/v1/major")
    suspend fun getMajorsByFaculty(@Query("facultyId") facultyId: String): ResponseDto<MajorResponseDto>

    @GET("/api/v1/major")
    suspend fun getMajorByNumberItem(@Query("limit") limit: Int): ResponseDto<MajorResponseDto>

    @GET("/api/v1/enrollment-year")
    suspend fun getEnrollmentYears(): ResponseDto<EnrollmentYearResponseDto>

    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/api/v1/register")
    suspend fun registerUser(@Body registerReq: RegisterReq): ResponseDto<RegisterResponseDto>
}