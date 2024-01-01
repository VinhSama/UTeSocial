package com.utesocial.android.feature_community.data.network.request

import com.utesocial.android.feature_community.domain.model.FriendRequest

data class AnswerFriendRequest(
    val status: FriendRequest.FriendState,
    val friendshipId: String
)