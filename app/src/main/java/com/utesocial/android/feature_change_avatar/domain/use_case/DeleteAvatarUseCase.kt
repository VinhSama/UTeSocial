package com.utesocial.android.feature_change_avatar.domain.use_case

import com.utesocial.android.feature_change_avatar.domain.repository.ChangeAvatarRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class DeleteAvatarUseCase(private val changeAvatarRepository: ChangeAvatarRepository) {

    operator fun invoke(): SimpleCall<AppResponse<Void>> = changeAvatarRepository.deleteAvatar()
}