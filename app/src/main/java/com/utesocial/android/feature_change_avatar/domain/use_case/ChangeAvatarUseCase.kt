package com.utesocial.android.feature_change_avatar.domain.use_case

data class ChangeAvatarUseCase(
    val uploadAvatarUseCase: UploadAvatarUseCase,
    val deleteAvatarUseCase: DeleteAvatarUseCase
)