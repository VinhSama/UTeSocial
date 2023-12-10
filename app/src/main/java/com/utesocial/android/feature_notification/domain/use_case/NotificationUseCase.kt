package com.utesocial.android.feature_notification.domain.use_case

data class NotificationUseCase(
    val getNotifies: GetNotifiesUseCase,
    val getRequests: GetRequestsUseCase
)