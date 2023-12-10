package com.utesocial.android.di.repository

import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository

interface AppRepository {

    val homeRepository: HomeRepository
    val notificationRepository: NotificationRepository
}