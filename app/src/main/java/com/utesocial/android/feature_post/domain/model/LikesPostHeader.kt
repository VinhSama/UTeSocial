package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LikesPostHeader(
    @SerializedName("_id")
    val likeId: String = "",
    val fullName: String = "",
    val userId: String = "",
    val isFriend: Boolean
) : Serializable {

}