package com.utesocial.android.feature_post.data.network

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.Query

interface PostApi {
    fun getFeedPosts(@Query("page") page: Int, @Query("limit") limit : Int, @Query("userType") userType: User.UserType) : SimpleCall<AppResponse<PostBody>>
}