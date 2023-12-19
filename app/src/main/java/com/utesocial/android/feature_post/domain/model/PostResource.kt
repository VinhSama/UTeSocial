package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName

data class PostResource (
    @SerializedName("_id")
    val id : String?,
    val url: String?,
    val resourceType: ResourceType
) {
    enum class ResourceType(val type: String) {
        IMAGE("Image"),
        VIDEO("Video")
    }
}