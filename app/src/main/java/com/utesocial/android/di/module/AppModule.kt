package com.utesocial.android.di.module

import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase

interface AppModule {

    val homeUseCase: HomeUseCase
    val notificationUseCase: NotificationUseCase
}