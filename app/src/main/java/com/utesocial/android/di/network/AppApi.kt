package com.utesocial.android.di.network

import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_notification.data.network.NotificationApi

interface AppApi {

    val homeApi: HomeApi
    val notificationApi: NotificationApi
}