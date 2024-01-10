package com.utesocial.android.feature_post.domain.model

sealed class PostInteraction(open val postModel: PostModel) {
    data class Like(override val postModel: PostModel) : PostInteraction(postModel)
    data class Unlike(override val postModel: PostModel) : PostInteraction(postModel)
}