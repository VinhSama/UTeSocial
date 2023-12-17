package com.utesocial.android.di.repository

import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository

interface AppRepository {

    val communityRepository: CommunityRepository
    val homeRepository: HomeRepository
    val notificationRepository: NotificationRepository
    val loginRepository: LoginRepository
}