package com.utesocial.android.feature_post.data.network.request

data class CreatePostRequest(
    val content: String = "",
    val postResources: List<String> = emptyList()
) {
    override fun toString(): String {
        return "CreatePostRequest(content='$content', postResources=$postResources)"
    }
}