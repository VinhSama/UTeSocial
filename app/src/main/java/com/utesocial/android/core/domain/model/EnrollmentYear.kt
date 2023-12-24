package com.utesocial.android.core.domain.model

import com.google.gson.annotations.SerializedName

data class EnrollmentYear(
    @SerializedName("_id")
    var id: String = "",
    var name: String = "",
    var startYear: Int = 0
)