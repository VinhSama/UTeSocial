package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class CreatePostUseCase(private val postRepository: PostRepository) {
    operator fun invoke(createPostRequest: CreatePostRequest) : SimpleCall<AppResponse<PostModel>> {
        return postRepository.createPost(createPostRequest)
    }
}