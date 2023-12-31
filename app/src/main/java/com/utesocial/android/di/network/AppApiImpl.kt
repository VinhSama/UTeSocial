package com.utesocial.android.di.network

import com.utesocial.android.di.util.Constants.BASE_URL
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppApiImpl(
    loginApi: LoginApi,
    postApi: PostApi,
    communityApi: CommunityApi,
    profileApi: ProfileApi,
    changeAvatarApi: ChangeAvatarApi,
    changePasswordApi: ChangePasswordApi,
    searchApi: SearchApi
) : AppApi {

    override val registerApi: RegisterApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RegisterApi::class.java) }

    override val communityApi: CommunityApi by lazy { communityApi }

    override val homeApi: HomeApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(HomeApi::class.java) }

    override val notificationApi: NotificationApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NotificationApi::class.java) }

    override val loginApi: LoginApi by lazy { loginApi }

    override val postApi: PostApi by lazy { postApi }

    override val profileApi: ProfileApi by lazy { profileApi }


    override val settingsApi: SettingsApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(SettingsApi::class.java) }

    override val changeAvatarApi: ChangeAvatarApi by lazy { changeAvatarApi }

    override val changePasswordApi: ChangePasswordApi by lazy { changePasswordApi }

    override val searchApi: SearchApi by lazy { searchApi }
}