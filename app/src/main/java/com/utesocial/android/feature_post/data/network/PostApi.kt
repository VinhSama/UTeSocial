package com.utesocial.android.feature_post.data.network

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.data.network.dto.PostResourcesBody
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    @GET("posts/new-feeds")
    fun getFeedPosts(@Query("page") page: Int, @Query("limit") limit : Int, @Query("userType") userType: Like.UserType) : SimpleCall<AppResponse<PostBody>>

    @POST("posts/uploads")
    fun uploadPostResources(@Body attachments: RequestBody) : SimpleCall<AppResponse<PostResourcesBody>>

    @POST("posts")
    fun createPost(@Body createPostRequest: CreatePostRequest) : SimpleCall<AppResponse<PostModel>>
}