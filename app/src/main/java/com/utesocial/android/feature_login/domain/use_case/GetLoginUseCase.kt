package com.utesocial.android.feature_login.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_login.data.network.dto.LoginBody
import com.utesocial.android.feature_login.data.network.request.LoginRequest
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetLoginUseCase(private val loginRepository: LoginRepository) {

    operator fun invoke(loginRequest: LoginRequest) : SimpleCall<AppResponse<LoginBody>> {
        return loginRepository.login(loginRequest)
    }
}