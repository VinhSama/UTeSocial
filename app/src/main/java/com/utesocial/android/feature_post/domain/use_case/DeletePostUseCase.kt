package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class DeletePostUseCase(private val postRepository: PostRepository) {

    operator fun invoke(postId: String): SimpleCall<AppResponse<Void>> =
        postRepository.deletePost(postId)
}