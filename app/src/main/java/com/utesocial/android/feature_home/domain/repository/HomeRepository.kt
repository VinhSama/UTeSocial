package com.utesocial.android.feature_home.domain.repository

import com.utesocial.android.feature_post.data.network.dto.PostDto

interface HomeRepository {

    suspend fun getSuggestPosts(): List<PostDto>
}