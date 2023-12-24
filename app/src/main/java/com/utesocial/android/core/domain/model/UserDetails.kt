package com.utesocial.android.core.domain.model

import java.io.Serializable

data class UserDetails (
    var graduated: Boolean = false,
    var classCode: String = "",
    var faculty: Faculty = Faculty(),
    var major: Major = Major(),
    var enrollmentYear: EnrollmentYear = EnrollmentYear(),
    var registeredMajor: Major = Major(),
    var highSchool: String = ""
) : Serializable