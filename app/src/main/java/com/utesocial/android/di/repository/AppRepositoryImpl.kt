package com.utesocial.android.di.repository

import com.utesocial.android.di.network.AppApi
import com.utesocial.android.feature_community.data.repository.CommunityRepositoryImpl
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_home.data.repository.HomeRepositoryImpl
import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_login.data.repository.LoginRepositoryImpl
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.feature_notification.data.repository.NotificationRepositoryImpl
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository
import com.utesocial.android.feature_post.data.repository.PostRepositoryImpl
import com.utesocial.android.feature_post.domain.repository.PostRepository

class AppRepositoryImpl(private val appApi: AppApi) : AppRepository {

    override val communityRepository: CommunityRepository by lazy { CommunityRepositoryImpl(appApi.communityApi) }

    override val homeRepository: HomeRepository by lazy { HomeRepositoryImpl(appApi.homeApi) }

    override val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl(appApi.notificationApi) }

    override val loginRepository: LoginRepository by lazy { LoginRepositoryImpl(appApi.loginApi) }

    override val postRepository: PostRepository by lazy { PostRepositoryImpl(appApi.postApi)}
}