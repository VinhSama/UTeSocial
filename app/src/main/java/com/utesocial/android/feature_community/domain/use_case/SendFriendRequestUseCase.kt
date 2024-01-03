package com.utesocial.android.feature_community.domain.use_case

import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SendFriendRequestUseCase(private val communityRepository: CommunityRepository) {

    operator fun invoke(receiverId: String): SimpleCall<AppResponse<Void>> =
        communityRepository.sendFriendRequest(receiverId)
}