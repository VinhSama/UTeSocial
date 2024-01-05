package com.utesocial.android.core.domain.model

import java.io.Serializable

data class EnrollmentYear(
    val name: String?,
    val startYear: Int?
) : Serializable {
    override fun toString(): String {
        return "EnrollmentYear(name=$name, startYear=$startYear)"
    }
}