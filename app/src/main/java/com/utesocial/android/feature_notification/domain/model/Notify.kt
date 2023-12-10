package com.utesocial.android.feature_notification.domain.model

data class Notify(
    val avatar: String,
    val content: String,
    val date: String,
    val unread: Boolean
)