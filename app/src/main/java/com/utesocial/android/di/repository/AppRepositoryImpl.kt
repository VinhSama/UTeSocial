package com.utesocial.android.di.repository

import com.utesocial.android.di.network.AppApi
import com.utesocial.android.feature_community.data.repository.CommunityRepositoryImpl
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_home.data.repository.HomeRepositoryImpl
import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_notification.data.repository.NotificationRepositoryImpl
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository

class AppRepositoryImpl(private val appApi: AppApi) : AppRepository {

    override val communityRepository: CommunityRepository by lazy { CommunityRepositoryImpl(appApi.communityApi) }

    override val homeRepository: HomeRepository by lazy { HomeRepositoryImpl(appApi.homeApi) }

    override val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl(appApi.notificationApi) }
}