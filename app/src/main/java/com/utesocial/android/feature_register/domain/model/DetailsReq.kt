package com.utesocial.android.feature_register.domain.model

sealed class DetailsReq {

    data class StudentReq(
        var graduated: Boolean = false,
        var classCode: String = "",
        var faculty: String= "",
        var major: String = "",
        var enrollmentYear: String = ""
    ) : DetailsReq()

    data class LecturerReq(var faculty: String = "") : DetailsReq()

    data class CandidateReq(
        var registeredMajor: String = "",
        var highSchool: String = ""
    ) : DetailsReq()
}