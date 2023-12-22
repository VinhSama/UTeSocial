package com.utesocial.android.feature_community.data.network

import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import retrofit2.http.GET

interface CommunityApi {

    @GET("")
    suspend fun getCommunityInfo(): List<CommunityDto>
}