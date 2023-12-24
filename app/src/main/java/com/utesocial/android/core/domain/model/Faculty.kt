package com.utesocial.android.core.domain.model

import com.google.gson.annotations.SerializedName

data class Faculty(
    @SerializedName("_id")
    var id: String = "",
    var code: String = "",
    var name: String = ""
)
