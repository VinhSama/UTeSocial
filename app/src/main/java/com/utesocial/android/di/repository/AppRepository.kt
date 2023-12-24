package com.utesocial.android.di.repository

import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import com.utesocial.android.feature_home.domain.repository.HomeRepository
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.feature_settings.domain.repository.SettingsRepository

interface AppRepository {

    val registerRepository: RegisterRepository
    val communityRepository: CommunityRepository
    val homeRepository: HomeRepository
    val notificationRepository: NotificationRepository
    val loginRepository: LoginRepository
    val postRepository: PostRepository
    val settingsRepository: SettingsRepository
    val profileRepository: ProfileRepository
}