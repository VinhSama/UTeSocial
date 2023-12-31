package com.utesocial.android.feature_community.domain.repository

import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import com.utesocial.android.feature_community.data.network.dto.FriendRequestsResponse
import com.utesocial.android.feature_community.data.network.dto.FriendsListResponse
import com.utesocial.android.feature_community.data.network.dto.SearchUserResponse
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface CommunityRepository {

    suspend fun getCommunityInfo(): List<CommunityDto>

    fun searchUsers(page: Int, limit: Int, search: String): SimpleCall<AppResponse<SearchUserResponse>>
    fun getFriendsList(page: Int, limit: Int, search: String): SimpleCall<AppResponse<FriendsListResponse>>
    fun getFriendRequest(page: Int, limit: Int, search: String): SimpleCall<AppResponse<FriendRequestsResponse>>
}