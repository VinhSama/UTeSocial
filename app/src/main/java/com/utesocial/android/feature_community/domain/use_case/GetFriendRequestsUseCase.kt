package com.utesocial.android.feature_community.domain.use_case

import com.utesocial.android.feature_community.data.network.dto.FriendRequestsResponse
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetFriendRequestsUseCase(private val communityRepository: CommunityRepository) {
    operator fun invoke(page: Int, limit: Int, search: String = ""): SimpleCall<AppResponse<FriendRequestsResponse>> {
        return communityRepository.getFriendRequest(page, limit, search)
    }
}