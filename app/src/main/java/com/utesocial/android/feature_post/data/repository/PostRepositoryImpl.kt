package com.utesocial.android.feature_post.data.repository

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.PostApi
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class PostRepositoryImpl(private val postApi: PostApi) : PostRepository {
    override fun getFeedPosts(page: Int, limit: Int, userType: User.UserType): SimpleCall<AppResponse<PostBody>> {
        return postApi.getFeedPosts(page, limit, userType)
    }
}