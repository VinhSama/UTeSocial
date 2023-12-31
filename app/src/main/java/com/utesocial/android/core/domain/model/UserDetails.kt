package com.utesocial.android.core.domain.model

import androidx.room.Embedded
import java.io.Serializable

data class UserDetails (
    var graduated: Boolean,
    var classCode: String,
    @Embedded(prefix = "faculty_")
    var faculty: Faculty?,
    @Embedded(prefix = "major_")
    var major: Major?,
    @Embedded(prefix = "enrollmentYear_")
    var enrollmentYear: EnrollmentYear?,
    @Embedded(prefix = "registeredMajor_")
    val registeredMajor: Major?,
    val highSchool: String?
) : Serializable {
    override fun toString(): String {
        return "UserDetails(graduated=$graduated, classCode='$classCode', faculty=$faculty, major=$major, enrollmentYear=$enrollmentYear, registeredMajor=$registeredMajor, highSchool=$highSchool)"
    }
}