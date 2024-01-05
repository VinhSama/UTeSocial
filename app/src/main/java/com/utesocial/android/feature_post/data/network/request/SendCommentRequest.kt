package com.utesocial.android.feature_post.data.network.request

data class SendCommentRequest(
    val text: String = "",
    val images: List<String> = emptyList()
) {
    override fun toString(): String {
        return "SendCommentRequest(text='$text', images=$images)"
    }
}