package com.utesocial.android.feature_community.data.network.dto

import com.utesocial.android.core.domain.model.User

data class SearchUserResponse(
    val users: List<UserWithFriendState> = emptyList(),
    val totalCount: Int
) {
    override fun toString(): String {
        return "SearchUserResponse(users=$users, totalCount=$totalCount)"
    }
}

data class UserWithFriendState(
    val user: User,
    val friendState: String
) {
    override fun toString(): String {
        return "UserWithFriendState(user=$user, friendState='$friendState')"
    }
}