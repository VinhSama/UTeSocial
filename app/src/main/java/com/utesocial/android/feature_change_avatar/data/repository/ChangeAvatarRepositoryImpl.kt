package com.utesocial.android.feature_change_avatar.data.repository

import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_change_avatar.data.network.ChangeAvatarApi
import com.utesocial.android.feature_change_avatar.domain.repository.ChangeAvatarRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

class ChangeAvatarRepositoryImpl(private val changeAvatarApi: ChangeAvatarApi) : ChangeAvatarRepository {

    override fun uploadAvatar(image: RequestBody): SimpleCall<AppResponse<Avatar>> =
        changeAvatarApi.uploadAvatar(image = image)

    override fun deleteAvatar(): SimpleCall<AppResponse<Void>> =
        changeAvatarApi.deleteAvatar()
}