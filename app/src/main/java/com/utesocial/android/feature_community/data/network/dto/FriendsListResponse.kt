package com.utesocial.android.feature_community.data.network.dto

import com.utesocial.android.core.domain.model.User

data class FriendsListResponse(
    val friends: List<User>,
    val totalCount: Int
) {

}