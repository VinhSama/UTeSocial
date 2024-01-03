package com.utesocial.android.feature_community.data.repository

import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import com.utesocial.android.feature_community.data.network.dto.FriendRequestsResponse
import com.utesocial.android.feature_community.data.network.dto.FriendsListResponse
import com.utesocial.android.feature_community.data.network.dto.SearchUserResponse
import com.utesocial.android.feature_community.data.network.request.AnswerFriendRequest
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class CommunityRepositoryImpl(private val communityApi: CommunityApi) : CommunityRepository {

    override suspend fun getCommunityInfo(): List<CommunityDto> = communityApi.getCommunityInfo()
    override fun searchUsers(
        page: Int,
        limit: Int,
        search: String
    ): SimpleCall<AppResponse<SearchUserResponse>> {
        return communityApi.searchUsers(page, limit, search)
    }

    override fun getFriendsList(
        page: Int,
        limit: Int,
        search: String
    ): SimpleCall<AppResponse<FriendsListResponse>> {
        return communityApi.getFriendsList(page, limit, search)
    }

    override fun getFriendRequest(
        page: Int,
        limit: Int,
        search: String
    ): SimpleCall<AppResponse<FriendRequestsResponse>> {
        return communityApi.getFriendRequest(page, limit, search)
    }

    override fun answerFriendRequest(answerFriendRequest: AnswerFriendRequest): SimpleCall<AppResponse<Int>> {
        return communityApi.answerFriendRequest(answerFriendRequest)
    }

    override fun sendFriendRequest(receiverId: String): SimpleCall<AppResponse<Void>> =
        communityApi.sendFriendRequest(receiverId)
}