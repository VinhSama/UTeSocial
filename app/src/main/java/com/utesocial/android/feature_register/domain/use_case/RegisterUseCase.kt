package com.utesocial.android.feature_register.domain.use_case

data class RegisterUseCase(
    val getFacultiesUseCase: GetFacultiesUseCase,
    val getMajorsByFacultyUseCase: GetMajorsByFacultyUseCase,
    val getMajorsByNumberItemUseCase: GetMajorsByNumberItemUseCase,
    val getEnrollmentYearsUseCase: GetEnrollmentYearsUseCase,
    val registerUserUseCase: RegisterUserUseCase
)