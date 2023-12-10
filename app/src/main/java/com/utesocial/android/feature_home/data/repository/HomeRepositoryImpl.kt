package com.utesocial.android.feature_home.data.repository

import com.utesocial.android.core.data.network.dto.PostDto
import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_home.domain.repository.HomeRepository

class HomeRepositoryImpl(private val homeApi: HomeApi) : HomeRepository {

    override suspend fun getSuggestPosts(): List<PostDto> = homeApi.getSuggestPosts()
}