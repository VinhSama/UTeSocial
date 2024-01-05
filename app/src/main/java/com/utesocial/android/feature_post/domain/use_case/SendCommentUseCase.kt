package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.SendCommentResponse
import com.utesocial.android.feature_post.data.network.request.SendCommentRequest
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SendCommentUseCase(private val postRepository: PostRepository) {
    operator fun invoke(postId: String, sendCommentRequest: SendCommentRequest) : SimpleCall<AppResponse<SendCommentResponse>> {
        return postRepository.sendComment(postId, sendCommentRequest)
    }
}