package com.utesocial.android.feature_change_avatar.domain.use_case

import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.feature_change_avatar.domain.repository.ChangeAvatarRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

class UploadAvatarUseCase(private val changeAvatarRepository: ChangeAvatarRepository) {

    operator fun invoke(image: RequestBody): SimpleCall<AppResponse<Avatar>> =
        changeAvatarRepository.uploadAvatar(image)
}