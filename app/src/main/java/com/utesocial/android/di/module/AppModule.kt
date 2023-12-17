package com.utesocial.android.di.module

import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase

interface AppModule {

    val communityUseCase: CommunityUseCase
    val homeUseCase: HomeUseCase
    val notificationUseCase: NotificationUseCase
    val loginUseCase: LoginUseCase
}