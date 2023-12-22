package com.utesocial.android.core.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Avatar(
    @SerializedName("_id")
    val id: String,
    val url: String
) : Serializable {
}