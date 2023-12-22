package com.utesocial.android.core.domain.model

import java.io.Serializable

data class UserDetails (
    var graduated: Boolean,
    var classCode: String,
    var faculty: String,
    var major: String,
    var enrollmentYear: String
) : Serializable