package com.utesocial.android.feature_community.domain.use_case

import com.utesocial.android.feature_community.data.network.request.AnswerFriendRequest
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class AnswerFriendRequestUseCase(private val communityRepository: CommunityRepository) {
    operator fun invoke(answerFriendRequest: AnswerFriendRequest) : SimpleCall<AppResponse<Int>> {
        return communityRepository.answerFriendRequest(answerFriendRequest)
    }
}