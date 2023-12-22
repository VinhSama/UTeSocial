package com.utesocial.android.feature_post.data.network

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    @GET("posts/new-feeds")
    fun getFeedPosts(@Query("page") page: Int, @Query("limit") limit : Int, @Query("userType") userType: Like.UserType) : SimpleCall<AppResponse<PostBody>>
}