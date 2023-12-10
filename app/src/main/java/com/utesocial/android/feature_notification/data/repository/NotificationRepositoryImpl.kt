package com.utesocial.android.feature_notification.data.repository

import com.utesocial.android.feature_notification.data.network.NotificationApi
import com.utesocial.android.feature_notification.data.network.dto.NotifyDto
import com.utesocial.android.feature_notification.data.network.dto.RequestDto
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository

class NotificationRepositoryImpl(private val notificationApi: NotificationApi) : NotificationRepository {

    override suspend fun getNotifies(): List<NotifyDto> = notificationApi.getNotifies()

    override suspend fun getRequests(): List<RequestDto> = notificationApi.getRequests()
}