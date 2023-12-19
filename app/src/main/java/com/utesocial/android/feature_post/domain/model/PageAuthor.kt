package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.domain.model.Avatar

data class PageAuthor(
    @SerializedName("_id")
    val id : String?,
    val pageName : String?,
    val avatar: Avatar?
)