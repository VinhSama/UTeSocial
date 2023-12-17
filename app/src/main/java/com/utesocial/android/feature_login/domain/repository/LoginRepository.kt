package com.utesocial.android.feature_login.domain.repository

import com.utesocial.android.core.data.network.dto.TokensBody
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_login.data.network.dto.LoginBody
import com.utesocial.android.feature_login.data.network.request.LoginRequest
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface LoginRepository {
    fun login(loginRequest: LoginRequest) : SimpleCall<AppResponse<LoginBody>>
    fun refreshToken(bodyRequest: HashMap<String, String>) : SimpleCall<AppResponse<TokensBody>>
}