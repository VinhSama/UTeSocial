package com.utesocial.android.feature_change_avatar.domain.repository

import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

interface ChangeAvatarRepository {

    fun uploadAvatar(image: RequestBody): SimpleCall<AppResponse<Avatar>>

    fun deleteAvatar(): SimpleCall<AppResponse<Void>>
}