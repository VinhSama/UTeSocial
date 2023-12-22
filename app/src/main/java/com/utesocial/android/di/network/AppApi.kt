package com.utesocial.android.di.network

import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_home.data.network.HomeApi
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_notification.data.network.NotificationApi
import com.utesocial.android.feature_register.data.network.RegisterApi
import com.utesocial.android.feature_post.data.network.PostApi

interface AppApi {

    val registerApi: RegisterApi

    val communityApi: CommunityApi
    val homeApi: HomeApi
    val notificationApi: NotificationApi
    val loginApi: LoginApi
    val postApi: PostApi
}