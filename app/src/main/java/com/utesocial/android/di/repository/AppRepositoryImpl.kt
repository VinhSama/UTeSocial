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
import com.utesocial.android.feature_register.data.repository.RegisterRepositoryImpl
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import com.utesocial.android.feature_post.data.repository.PostRepositoryImpl
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.feature_profile.data.repository.ProfileRepositoryImpl
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.feature_settings.data.repository.SettingsRepositoryImpl
import com.utesocial.android.feature_settings.domain.repository.SettingsRepository

class AppRepositoryImpl(private val appApi: AppApi) : AppRepository {

    override val registerRepository: RegisterRepository by lazy { RegisterRepositoryImpl(appApi.registerApi) }

    override val communityRepository: CommunityRepository by lazy { CommunityRepositoryImpl(appApi.communityApi) }

    override val homeRepository: HomeRepository by lazy { HomeRepositoryImpl(appApi.homeApi) }

    override val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl(appApi.notificationApi) }

    override val loginRepository: LoginRepository by lazy { LoginRepositoryImpl(appApi.loginApi) }

    override val postRepository: PostRepository by lazy { PostRepositoryImpl(appApi.postApi)}

    override val settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl(appApi.settingsApi) }

    override val profileRepository: ProfileRepository by lazy { ProfileRepositoryImpl(appApi.profileApi) }
}