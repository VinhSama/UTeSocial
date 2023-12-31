package com.utesocial.android.feature_change_password.domain.repository

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_change_password.domain.model.ChangePasswordReq
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse

interface ChangePasswordRepository {

    fun changePassword(changePasswordReq: ChangePasswordReq): SimpleCall<AppResponse<Void>>
}