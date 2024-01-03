package com.utesocial.android.di.module

import com.utesocial.android.core.domain.use_case.MainUseCase
import com.utesocial.android.feature_change_avatar.domain.use_case.ChangeAvatarUseCase
import com.utesocial.android.feature_change_password.domain.use_case.ChangePasswordUseCase
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase
import com.utesocial.android.feature_register.domain.use_case.RegisterUseCase
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.feature_profile.domain.use_case.ProfileUseCase
import com.utesocial.android.feature_search.domain.use_case.SearchUserUseCase
import com.utesocial.android.feature_settings.domain.use_case.SettingsUseCase

interface AppModule {

    val registerUseCase: RegisterUseCase
    val communityUseCase: CommunityUseCase
    val homeUseCase: HomeUseCase
    val notificationUseCase: NotificationUseCase
    val loginUseCase: LoginUseCase
    val postUseCase: PostUseCase
    val settingsUseCase: SettingsUseCase
    val changeAvatarUseCase: ChangeAvatarUseCase
    val changePasswordUseCase: ChangePasswordUseCase
    val profileUseCase: ProfileUseCase
    val mainUseCase: MainUseCase
}