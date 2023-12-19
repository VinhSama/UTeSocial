package com.utesocial.android.feature_login.data.network

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.core.data.network.dto.TokensBody
import com.utesocial.android.feature_login.data.network.dto.LoginBody
import com.utesocial.android.feature_login.data.network.request.LoginRequest
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("refresh-token")
    fun refreshToken(@Body bodyRequest: HashMap<String, String>) : SimpleCall<AppResponse<TokensBody>>
    @POST("login")
    fun login(@Body loginRequest: LoginRequest) : SimpleCall<AppResponse<LoginBody>>
}