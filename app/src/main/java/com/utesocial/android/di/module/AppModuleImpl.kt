package com.utesocial.android.di.module

import com.utesocial.android.core.domain.use_case.MainUseCase
import com.utesocial.android.di.repository.AppRepository
import com.utesocial.android.feature_change_avatar.domain.use_case.ChangeAvatarUseCase
import com.utesocial.android.feature_change_avatar.domain.use_case.DeleteAvatarUseCase
import com.utesocial.android.feature_change_avatar.domain.use_case.UploadAvatarUseCase
import com.utesocial.android.feature_change_password.domain.use_case.ChangePasswordUseCase
import com.utesocial.android.feature_community.domain.use_case.AnswerFriendRequestUseCase
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.domain.use_case.GetCommunityInfoUseCase
import com.utesocial.android.feature_community.domain.use_case.GetFriendRequestsUseCase
import com.utesocial.android.feature_community.domain.use_case.GetFriendsListUseCase
import com.utesocial.android.feature_community.domain.use_case.SearchUserUseCase
import com.utesocial.android.feature_community.domain.use_case.SendFriendRequestUseCase
import com.utesocial.android.feature_home.domain.use_case.GetSuggestPostsUseCase
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_login.domain.use_case.GetLoginUseCase
import com.utesocial.android.feature_login.domain.use_case.GetRefreshTokenUseCase
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_notification.domain.use_case.GetNotifiesUseCase
import com.utesocial.android.feature_notification.domain.use_case.GetRequestsUseCase
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase
import com.utesocial.android.feature_post.domain.use_case.ChangePrivacyUseCase
import com.utesocial.android.feature_post.domain.use_case.CreatePostUseCase
import com.utesocial.android.feature_register.domain.use_case.GetEnrollmentYearsUseCase
import com.utesocial.android.feature_register.domain.use_case.GetFacultiesUseCase
import com.utesocial.android.feature_register.domain.use_case.GetMajorsByFacultyUseCase
import com.utesocial.android.feature_register.domain.use_case.GetMajorsByNumberItemUseCase
import com.utesocial.android.feature_register.domain.use_case.RegisterUseCase
import com.utesocial.android.feature_register.domain.use_case.RegisterUserUseCase
import com.utesocial.android.feature_post.domain.use_case.GetFeedPostsUseCase
import com.utesocial.android.feature_post.domain.use_case.LikePostUseCase
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.feature_post.domain.use_case.UnlikePostUseCase
import com.utesocial.android.feature_settings.domain.use_case.LogoutUseCase
import com.utesocial.android.feature_settings.domain.use_case.SettingsUseCase
import com.utesocial.android.feature_post.domain.use_case.UploadPostResourcesUseCase
import com.utesocial.android.feature_post.domain.use_case.DeletePostUseCase
import com.utesocial.android.feature_post.domain.use_case.GetCommentsByPostIdUseCase
import com.utesocial.android.feature_profile.domain.use_case.GetMyPostsUseCase
import com.utesocial.android.feature_profile.domain.use_case.ProfileUseCase
import com.utesocial.android.feature_profile.domain.use_case.UpdateUsernameUseCase

class AppModuleImpl(private val appRepository: AppRepository) : AppModule {

    override val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(
        getFacultiesUseCase = GetFacultiesUseCase(appRepository.registerRepository),
        getMajorsByFacultyUseCase = GetMajorsByFacultyUseCase(appRepository.registerRepository),
        getMajorsByNumberItemUseCase = GetMajorsByNumberItemUseCase(appRepository.registerRepository),
        getEnrollmentYearsUseCase = GetEnrollmentYearsUseCase(appRepository.registerRepository),
        registerUserUseCase = RegisterUserUseCase(appRepository.registerRepository)
    ) }

    override val communityUseCase: CommunityUseCase by lazy { CommunityUseCase(
        getCommunityInfoUseCase = GetCommunityInfoUseCase(appRepository.communityRepository),
        searchUserUseCase = SearchUserUseCase(appRepository.communityRepository),
        getFriendsListUseCase = GetFriendsListUseCase(appRepository.communityRepository),
        getFriendRequestsUseCase = GetFriendRequestsUseCase(appRepository.communityRepository),
        answerFriendRequestUseCase = AnswerFriendRequestUseCase(appRepository.communityRepository)
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
        getFeedPostsUseCase = GetFeedPostsUseCase(appRepository.postRepository),
        uploadPostResourcesUseCase = UploadPostResourcesUseCase(appRepository.postRepository),
        createPostUseCase = CreatePostUseCase(appRepository.postRepository),
        likePostUseCase = LikePostUseCase(appRepository.postRepository),
        unlikePostUseCase = UnlikePostUseCase(appRepository.postRepository),
        deletePostUseCase = DeletePostUseCase(appRepository.postRepository),
        changePrivacyUseCase = ChangePrivacyUseCase(appRepository.postRepository),
        getCommentsByPostIdUseCase = GetCommentsByPostIdUseCase(appRepository.postRepository)
    )}

    override val settingsUseCase: SettingsUseCase by lazy { SettingsUseCase(
        logoutUseCase = LogoutUseCase(appRepository.settingsRepository)
    ) }

    override val changeAvatarUseCase: ChangeAvatarUseCase by lazy { ChangeAvatarUseCase(
        uploadAvatarUseCase = UploadAvatarUseCase(appRepository.changeAvatarRepository),
        deleteAvatarUseCase = DeleteAvatarUseCase(appRepository.changeAvatarRepository)
    ) }

    override val changePasswordUseCase: ChangePasswordUseCase by lazy {
        ChangePasswordUseCase(appRepository.changePasswordRepository)
    }

    override val profileUseCase: ProfileUseCase by lazy { ProfileUseCase(
        updateUsernameUseCase = UpdateUsernameUseCase(appRepository.profileRepository),
        getMyPostsUseCase = GetMyPostsUseCase(appRepository.profileRepository),
        deletePostUseCase = DeletePostUseCase(appRepository.postRepository),
        changePrivacyUseCase = ChangePrivacyUseCase(appRepository.postRepository)
    ) }

    override val mainUseCase: MainUseCase by lazy { MainUseCase(
        searchUserUseCase = com.utesocial.android.feature_search.domain.use_case.SearchUserUseCase(appRepository.searchUserRepository),
        sendFriendRequestUseCase = SendFriendRequestUseCase(appRepository.communityRepository)
    ) }
}