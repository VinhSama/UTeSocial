package com.utesocial.android.di.network

import com.utesocial.android.feature_change_avatar.data.network.ChangeAvatarApi
import com.utesocial.android.feature_change_password.data.network.ChangePasswordApi
import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_notification.data.network.NotificationApi
import com.utesocial.android.feature_register.data.network.RegisterApi
import com.utesocial.android.feature_post.data.network.PostApi
import com.utesocial.android.feature_profile.data.network.ProfileApi
import com.utesocial.android.feature_search.data.network.SearchApi
import com.utesocial.android.feature_settings.data.network.SettingsApi

interface AppApi {

    val registerApi: RegisterApi
    val communityApi: CommunityApi
    val homeApi: HomeApi
    val notificationApi: NotificationApi
    val loginApi: LoginApi
    val postApi: PostApi
    val settingsApi: SettingsApi
    val profileApi: ProfileApi
    val changeAvatarApi: ChangeAvatarApi
    val changePasswordApi: ChangePasswordApi
    val searchApi: SearchApi
}