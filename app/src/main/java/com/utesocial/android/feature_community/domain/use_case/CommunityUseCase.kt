package com.utesocial.android.feature_community.domain.use_case

data class CommunityUseCase(
    val getCommunityInfoUseCase: GetCommunityInfoUseCase,
    val searchUserUseCase: SearchUserUseCase,
    val getFriendsListUseCase: GetFriendsListUseCase
)