package com.utesocial.android.feature_notification.domain.model

data class Request(
    val avatar: String,
    val date: String,
    val name: String,
    val mutualFriends: List<String>,
    val unread: Boolean
)