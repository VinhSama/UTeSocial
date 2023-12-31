package com.utesocial.android.feature_change_password.data.network

import com.utesocial.android.feature_change_password.domain.model.ChangePasswordReq
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.Body
import retrofit2.http.PUT

interface ChangePasswordApi {

    @PUT("change-password")
    fun changePassword(@Body changePasswordReq: ChangePasswordReq): SimpleCall<AppResponse<Void>>
}