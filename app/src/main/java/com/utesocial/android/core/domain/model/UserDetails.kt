package com.utesocial.android.core.domain.model

import androidx.room.Embedded
import java.io.Serializable

data class UserDetails (
    var graduated: Boolean = false,
    var classCode: String?,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserDetails

        if (graduated != other.graduated) return false
        if (classCode != other.classCode) return false
        if (faculty != other.faculty) return false
        if (major != other.major) return false
        if (enrollmentYear != other.enrollmentYear) return false
        if (registeredMajor != other.registeredMajor) return false
        return highSchool == other.highSchool
    }

    override fun hashCode(): Int {
        var result = graduated?.hashCode() ?: 0
        result = 31 * result + (classCode?.hashCode() ?: 0)
        result = 31 * result + (faculty?.hashCode() ?: 0)
        result = 31 * result + (major?.hashCode() ?: 0)
        result = 31 * result + (enrollmentYear?.hashCode() ?: 0)
        result = 31 * result + (registeredMajor?.hashCode() ?: 0)
        result = 31 * result + (highSchool?.hashCode() ?: 0)
        return result
    }


}