package com.utesocial.android.feature_home.data.network

import com.utesocial.android.feature_post.data.network.dto.PostDto
import retrofit2.http.GET

interface HomeApi {

    @GET("/")
    suspend fun getSuggestPosts(): List<PostDto>
}