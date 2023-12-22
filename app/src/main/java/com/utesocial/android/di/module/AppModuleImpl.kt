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
import com.utesocial.android.feature_register.domain.use_case.GetEnrollmentYearsUseCase
import com.utesocial.android.feature_register.domain.use_case.GetFacultiesUseCase
import com.utesocial.android.feature_register.domain.use_case.GetMajorsByFacultyUseCase
import com.utesocial.android.feature_register.domain.use_case.GetMajorsByNumberItemUseCase
import com.utesocial.android.feature_register.domain.use_case.RegisterUseCase
import com.utesocial.android.feature_register.domain.use_case.RegisterUserUseCase
import com.utesocial.android.feature_post.domain.use_case.GetFeedPostsUseCase
import com.utesocial.android.feature_post.domain.use_case.PostUseCase

class AppModuleImpl(private val appRepository: AppRepository) : AppModule {

    override val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(
        getFacultiesUseCase = GetFacultiesUseCase(appRepository.registerRepository),
        getMajorsByFacultyUseCase = GetMajorsByFacultyUseCase(appRepository.registerRepository),
        getMajorsByNumberItemUseCase = GetMajorsByNumberItemUseCase(appRepository.registerRepository),
        getEnrollmentYearsUseCase = GetEnrollmentYearsUseCase(appRepository.registerRepository),
        registerUserUseCase = RegisterUserUseCase(appRepository.registerRepository)
    ) }

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

    override val postUseCase : PostUseCase by lazy { PostUseCase(
        getFeedPostsUseCase = GetFeedPostsUseCase(appRepository.postRepository)
    )}
}