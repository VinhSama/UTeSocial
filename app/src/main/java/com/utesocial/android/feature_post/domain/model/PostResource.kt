package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostResource (
    @SerializedName("_id")
    val id : String = "",
    val url: String = "",
    val resourceType: ResourceType?
) : Serializable {
    enum class ResourceType() {
        @SerializedName("Image")
        IMAGE,
        @SerializedName("Video")
        VIDEO
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostResource

        if (id != other.id) return false
        if (url != other.url) return false
        return resourceType == other.resourceType
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (resourceType?.hashCode() ?: 0)
        return result
    }

}