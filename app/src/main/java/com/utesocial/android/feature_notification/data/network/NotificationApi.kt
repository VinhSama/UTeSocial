package com.utesocial.android.feature_notification.data.network

import com.utesocial.android.feature_notification.data.network.dto.NotifyDto
import com.utesocial.android.feature_notification.data.network.dto.RequestDto
import retrofit2.http.GET

interface NotificationApi {

    @GET("notifies")
    suspend fun getNotifies(): List<NotifyDto>

    @GET("request")
    suspend fun getRequests(): List<RequestDto>
}