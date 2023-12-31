package com.utesocial.android.feature_post.domain.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.CommentsResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.data.network.dto.PostResourcesBody
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.dto.SendCommentResponse
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.data.network.request.SendCommentRequest
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

interface PostRepository {
    fun getFeedPosts(
        page: Int,
        limit: Int,
        userType: Like.UserType
    ): SimpleCall<AppResponse<PostBody>>

    fun uploadPostResources(attachments: RequestBody): SimpleCall<AppResponse<PostResourcesBody>>
    fun createPost(createPostRequest: CreatePostRequest): SimpleCall<AppResponse<PostModel>>
    fun deletePost(postId: String): SimpleCall<AppResponse<Void>>
    fun likePost(postId: String): SimpleCall<AppResponse<Int>>
    fun unlikePost(postId: String): SimpleCall<AppResponse<Int>>

    fun changePrivacy(
        postId: String,
        privacyRequest: PrivacyRequest
    ): SimpleCall<AppResponse<PrivacyResponse>>
    fun getCommentsByPostId(
        postId: String, page: Int, limit: Int
    ): SimpleCall<AppResponse<CommentsResponse>>
    fun sendComment(
        postId: String, sendCommentRequest: SendCommentRequest
    ) : SimpleCall<AppResponse<SendCommentResponse>>
}