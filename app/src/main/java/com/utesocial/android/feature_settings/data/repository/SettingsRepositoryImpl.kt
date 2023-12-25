package com.utesocial.android.feature_settings.data.repository

import com.utesocial.android.core.data.network.dto.ResponseDto
import com.utesocial.android.feature_settings.data.network.SettingsApi
import com.utesocial.android.feature_settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val settingsApi: SettingsApi) : SettingsRepository {

    override suspend fun logout(accessToken: HashMap<String, String>): ResponseDto<*> = settingsApi.logout(accessToken)
}