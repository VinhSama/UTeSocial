package com.utesocial.android.feature_profile.domain.use_case

data class ProfileUseCase(
    val updateUsernameUseCase: UpdateUsernameUseCase,
    val getMyPostsUseCase: GetMyPostsUseCase,
    val deleteMyPostUseCase: DeleteMyPostUseCase
)