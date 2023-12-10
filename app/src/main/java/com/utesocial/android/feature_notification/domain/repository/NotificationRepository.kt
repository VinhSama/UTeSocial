package com.utesocial.android.feature_notification.domain.repository

import com.utesocial.android.feature_notification.data.network.dto.NotifyDto
import com.utesocial.android.feature_notification.data.network.dto.RequestDto

interface NotificationRepository {

    suspend fun getNotifies(): List<NotifyDto>

    suspend fun getRequests(): List<RequestDto>
}