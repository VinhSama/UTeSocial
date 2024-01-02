package com.utesocial.android.feature_profile.data.network

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {

    @PUT("update-info")
    fun updateUsername(@Body username: UsernameReq): SimpleCall<AppResponse<User>>

    @GET("posts/users/{userId}")
    fun getMyPosts(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): SimpleCall<AppResponse<PostBody>>
}