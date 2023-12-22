package com.utesocial.android.feature_login.domain.use_case

import com.utesocial.android.core.data.network.dto.TokensBody
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_login.domain.repository.LoginRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetRefreshTokenUseCase(private val loginRepository: LoginRepository) {

    operator fun invoke(bodyRequest: HashMap<String, String>) : SimpleCall<AppResponse<TokensBody>> {
        return loginRepository.refreshToken(bodyRequest)
    }
}