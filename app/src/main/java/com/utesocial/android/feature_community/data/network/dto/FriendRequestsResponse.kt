package com.utesocial.android.feature_community.data.network.dto

import com.utesocial.android.feature_community.domain.model.FriendRequest

data class FriendRequestsResponse(
    val requests: List<FriendRequest>,
    val totalCount: Int
) {
}