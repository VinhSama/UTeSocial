package com.utesocial.android.feature_community.data.repository

import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import com.utesocial.android.feature_community.domain.repository.CommunityRepository

class CommunityRepositoryImpl(private val communityApi: CommunityApi) : CommunityRepository {

    override suspend fun getJoinGroups(): List<CommunityDto> = communityApi.getJoinGroups()
}