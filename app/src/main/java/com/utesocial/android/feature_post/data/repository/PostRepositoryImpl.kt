package com.utesocial.android.feature_post.data.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.PostApi
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
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

class PostRepositoryImpl(private val postApi: PostApi) : PostRepository {

    override fun getFeedPosts(
        page: Int,
        limit: Int,
        userType: Like.UserType
    ): SimpleCall<AppResponse<PostBody>> {
        return postApi.getFeedPosts(page, limit, userType)
    }

    override fun uploadPostResources(attachments: RequestBody): SimpleCall<AppResponse<PostResourcesBody>> {
        return postApi.uploadPostResources(attachments)
    }

    override fun createPost(createPostRequest: CreatePostRequest): SimpleCall<AppResponse<PostModel>> {
        return postApi.createPost(createPostRequest)
    }

    override fun deletePost(postId: String): SimpleCall<AppResponse<Void>> =
        postApi.deletePost(postId)

    override fun likePost(postId: String): SimpleCall<AppResponse<Int>> {
        return postApi.likePost(postId)
    }

    override fun unlikePost(postId: String): SimpleCall<AppResponse<Int>> {
        return postApi.unlikePost(postId)
    }

    override fun changePrivacy(
        postId: String,
        privacyRequest: PrivacyRequest
    ): SimpleCall<AppResponse<PrivacyResponse>> = postApi.changePrivacy(postId, privacyRequest)

    override fun getCommentsByPostId(
        postId: String,
        page: Int,
        limit: Int
    ): SimpleCall<AppResponse<CommentsResponse>> {
        return postApi.getCommentsByPostId(postId, page, limit)
    }

    override fun sendComment(
        postId: String,
        sendCommentRequest: SendCommentRequest
    ): SimpleCall<AppResponse<SendCommentResponse>> {
        return postApi.sendComment(postId, sendCommentRequest)
    }

}