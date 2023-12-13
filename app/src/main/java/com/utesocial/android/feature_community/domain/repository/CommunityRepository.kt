package com.utesocial.android.feature_community.domain.repository

import com.utesocial.android.feature_community.data.network.dto.CommunityDto

interface CommunityRepository {

    suspend fun getCommunityInfo(): List<CommunityDto>
}