package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetFeedPostsUseCase(private val postRepository: PostRepository) {
    operator fun invoke(page: Int, limit: Int, userType: Like.UserType) : SimpleCall<AppResponse<PostBody>> {
        return postRepository.getFeedPosts(page, limit, userType)
    }
}