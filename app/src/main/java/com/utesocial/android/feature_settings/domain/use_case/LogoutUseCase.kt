package com.utesocial.android.feature_settings.domain.use_case

import com.utesocial.android.core.domain.util.Constants
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LogoutUseCase(private val settingsRepository: SettingsRepository) {

    operator fun invoke(accessToken: HashMap<String, String>): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val responseDto = settingsRepository.logout(accessToken)
            val message = responseDto.message

            if (responseDto.status == Constants.RESPONSE_SUCCESS) {
                emit(Resource.Success(data = message))
            } else {
                emit(Resource.Error(message = message))
            }
        }  catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}