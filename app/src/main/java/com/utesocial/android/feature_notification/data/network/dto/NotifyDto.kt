package com.utesocial.android.feature_notification.data.network.dto

import com.utesocial.android.feature_notification.domain.model.Notify

data class NotifyDto(
    val avatar: String,
    val content: String,
    val date: String,
    val unread: Boolean
) {

    fun toNotify(): Notify = Notify(
        avatar = avatar,
        content = content,
        date = date,
        unread = unread
    )
}