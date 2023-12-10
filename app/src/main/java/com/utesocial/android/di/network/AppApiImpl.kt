package com.utesocial.android.di.network

import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_notification.data.network.NotificationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppApiImpl : AppApi {

    override val homeApi: HomeApi by lazy { Retrofit.Builder().baseUrl("https://example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(HomeApi::class.java) }

    override val notificationApi: NotificationApi by lazy { Retrofit.Builder().baseUrl("https://example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NotificationApi::class.java) }
}