package com.utesocial.android.feature_notification.data.network.dto

import com.utesocial.android.feature_notification.domain.model.Request

data class RequestDto(
    val avatar: String,
    val date: String,
    val name: String,
    val mutualFriends: List<String>,
    val unread: Boolean
) {

    fun toRequest(): Request = Request(
        avatar = avatar,
        date = date,
        name = name,
        mutualFriends = mutualFriends,
        unread = unread
    )
}