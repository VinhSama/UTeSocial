package com.utesocial.android.core.domain.model

import java.io.Serializable

data class UserDetails (
    var graduated: Boolean,
    var classCode: String,
    var faculty: Faculty?,
    var major: Major?,
    var enrollmentYear: EnrollmentYear?,
    val registeredMajor: Major?,
    val highSchool: String?
) : Serializable {
    override fun toString(): String {
        return "UserDetails(graduated=$graduated, classCode='$classCode', faculty=$faculty, major=$major, enrollmentYear=$enrollmentYear, registeredMajor=$registeredMajor, highSchool=$highSchool)"
    }
}