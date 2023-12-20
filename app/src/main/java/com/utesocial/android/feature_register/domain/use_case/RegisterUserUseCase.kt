package com.utesocial.android.feature_register.domain.use_case

import com.utesocial.android.core.domain.util.Constants
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.domain.model.RegisterReq
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RegisterUserUseCase(private val registerRepository: RegisterRepository) {

    operator fun invoke(registerReq: RegisterReq): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val responseDto = registerRepository.registerUser(registerReq)
            if (responseDto.status == Constants.RESPONSE_SUCCESS) {
                emit(Resource.Success(data = "Registered successfully"))
            } else {
                emit(Resource.Error(message = responseDto.message))
            }
        }  catch (error: HttpException) {
            emit(Resource.Error(message = error.message ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}