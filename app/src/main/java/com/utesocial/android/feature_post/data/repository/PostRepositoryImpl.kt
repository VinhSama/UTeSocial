package com.utesocial.android.feature_post.data.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.PostApi
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class PostRepositoryImpl(private val postApi: PostApi) : PostRepository {

    override fun getFeedPosts(
        page: Int,
        limit: Int,
        userType: Like.UserType
    ): SimpleCall<AppResponse<PostBody>> {
        return postApi.getFeedPosts(page, limit, userType)
    }
}