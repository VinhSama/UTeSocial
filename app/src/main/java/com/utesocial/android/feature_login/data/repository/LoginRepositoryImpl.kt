package com.utesocial.android.feature_login.data.repository

import com.utesocial.android.core.data.network.dto.TokensBody
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_login.data.network.dto.LoginBody
import com.utesocial.android.feature_login.data.network.request.LoginRequest
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class LoginRepositoryImpl(private val loginApi: LoginApi) : LoginRepository {
    override fun login(loginRequest: LoginRequest): SimpleCall<AppResponse<LoginBody>> {
        return loginApi.login(loginRequest)
    }

    override fun refreshToken(bodyRequest: HashMap<String, String>): SimpleCall<AppResponse<TokensBody>> {
        return loginApi.refreshToken(bodyRequest)
    }

}