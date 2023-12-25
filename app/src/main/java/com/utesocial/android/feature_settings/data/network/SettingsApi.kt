package com.utesocial.android.feature_settings.data.network

import com.utesocial.android.core.data.network.dto.ResponseDto
import retrofit2.http.Body
import retrofit2.http.HTTP

interface SettingsApi {

    @HTTP(method = "DELETE", path = "logout", hasBody = true)
    suspend fun logout(@Body accessToken: HashMap<String, String>): ResponseDto<*>
}