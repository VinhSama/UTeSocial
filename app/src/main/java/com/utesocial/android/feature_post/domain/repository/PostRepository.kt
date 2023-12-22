package com.utesocial.android.feature_post.domain.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface PostRepository {
    fun getFeedPosts(page: Int, limit: Int, userType: Like.UserType) : SimpleCall<AppResponse<PostBody>>
}