package com.utesocial.android.di.module

import com.utesocial.android.di.repository.AppRepository
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.domain.use_case.GetCommunityInfoUseCase
import com.utesocial.android.feature_home.domain.use_case.GetSuggestPostsUseCase
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_login.domain.use_case.GetLoginUseCase
import com.utesocial.android.feature_login.domain.use_case.GetRefreshTokenUseCase
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_notification.domain.use_case.GetNotifiesUseCase
import com.utesocial.android.feature_notification.domain.use_case.GetRequestsUseCase
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase

class AppModuleImpl(private val appRepository: AppRepository) : AppModule {

    override val communityUseCase: CommunityUseCase by lazy { CommunityUseCase(
        getCommunityInfoUseCase = GetCommunityInfoUseCase(appRepository.communityRepository)
    ) }

    override val homeUseCase: HomeUseCase by lazy { HomeUseCase(
        getSuggestPostsUseCase = GetSuggestPostsUseCase(appRepository.homeRepository)
    ) }

    override val notificationUseCase: NotificationUseCase by lazy { NotificationUseCase(
        getNotifies = GetNotifiesUseCase(appRepository.notificationRepository),
        getRequests = GetRequestsUseCase(appRepository.notificationRepository)
    ) }
    override val loginUseCase: LoginUseCase by lazy { LoginUseCase(
        getLoginUseCase = GetLoginUseCase(appRepository.loginRepository),
        getRefreshTokenUseCase = GetRefreshTokenUseCase(appRepository.loginRepository)
    ) }
}