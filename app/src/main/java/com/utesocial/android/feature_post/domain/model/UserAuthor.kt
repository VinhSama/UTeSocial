package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.domain.model.Avatar
import java.io.Serializable

data class UserAuthor(
    @SerializedName("_id")
    val id : String?,
    val username: String?,
    val email : String?,
    val avatar: Avatar?,
) : Serializable