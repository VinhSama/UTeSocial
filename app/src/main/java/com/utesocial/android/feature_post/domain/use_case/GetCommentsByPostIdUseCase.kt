package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.CommentsResponse
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetCommentsByPostIdUseCase(private val postRepository: PostRepository) {
    operator fun invoke(postId: String, page: Int, limit: Int) : SimpleCall<AppResponse<CommentsResponse>> {
        return postRepository.getCommentsByPostId(postId, page, limit)
    }
}