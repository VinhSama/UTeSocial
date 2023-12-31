package com.utesocial.android.feature_change_password.data.repository

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_change_password.data.network.ChangePasswordApi
import com.utesocial.android.feature_change_password.domain.model.ChangePasswordReq
import com.utesocial.android.feature_change_password.domain.repository.ChangePasswordRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse

class ChangePasswordRepositoryImpl(private val changePasswordApi: ChangePasswordApi) : ChangePasswordRepository {

    override fun changePassword(changePasswordReq: ChangePasswordReq): SimpleCall<AppResponse<Void>>
    = changePasswordApi.changePassword(changePasswordReq)
}