package com.utesocial.android.feature_post.domain.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.data.network.dto.PostResourcesBody
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

interface PostRepository {
    fun getFeedPosts(page: Int, limit: Int, userType: Like.UserType) : SimpleCall<AppResponse<PostBody>>
    fun uploadPostResources(attachments: RequestBody) : SimpleCall<AppResponse<PostResourcesBody>>
    fun createPost(createPostRequest: CreatePostRequest) : SimpleCall<AppResponse<PostModel>>
}