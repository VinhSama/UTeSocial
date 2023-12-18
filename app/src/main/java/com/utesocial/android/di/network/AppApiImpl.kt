package com.utesocial.android.di.network

import com.utesocial.android.di.util.Constants.BASE_URL
import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_notification.data.network.NotificationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppApiImpl(
    loginApi: LoginApi
) : AppApi {

    override val communityApi: CommunityApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(CommunityApi::class.java) }

    override val homeApi: HomeApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(HomeApi::class.java) }

    override val notificationApi: NotificationApi by lazy { Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NotificationApi::class.java) }

    override val loginApi: LoginApi by lazy { loginApi }
}