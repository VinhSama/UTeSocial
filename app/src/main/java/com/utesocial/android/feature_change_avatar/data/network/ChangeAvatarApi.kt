package com.utesocial.android.feature_change_avatar.data.network

import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface ChangeAvatarApi {

    @PUT("avatar")
    fun uploadAvatar(@Body image: RequestBody): SimpleCall<AppResponse<Avatar>>

    @DELETE("avatar")
    fun deleteAvatar(): SimpleCall<AppResponse<Void>>
}