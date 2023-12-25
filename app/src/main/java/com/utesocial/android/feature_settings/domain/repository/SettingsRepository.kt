package com.utesocial.android.feature_settings.domain.repository

import com.utesocial.android.core.data.network.dto.ResponseDto

interface SettingsRepository {

    suspend fun logout(accessToken: HashMap<String, String>): ResponseDto<*>
}