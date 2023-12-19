package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName

class PostModel(
    @SerializedName("_id")
    val id : String,
    val userAuthor: UserAuthor,
    val userPageAuthor: PageAuthor,
    val group : String,
    val content : String,
    val postResource: PostResource,
    val likeCounts : Int,
    val likes : List<Like>,
    val sharedPost : String,
    val privacyMode : Int,
    val tags : List<String>,
    val shares : Int
) {
}