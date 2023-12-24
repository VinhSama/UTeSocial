package com.utesocial.android.core.domain.model

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class Major(
    @SerializedName("_id")
    var id: String = "",
    var code: String = "",
    var name: LinkedTreeMap<String, String> = LinkedTreeMap()
)