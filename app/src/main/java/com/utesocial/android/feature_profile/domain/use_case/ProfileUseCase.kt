package com.utesocial.android.feature_profile.domain.use_case

import com.utesocial.android.feature_post.domain.use_case.DeletePostUseCase

data class ProfileUseCase(
    val updateUsernameUseCase: UpdateUsernameUseCase,
    val getMyPostsUseCase: GetMyPostsUseCase,
    val deletePostUseCase: DeletePostUseCase
)