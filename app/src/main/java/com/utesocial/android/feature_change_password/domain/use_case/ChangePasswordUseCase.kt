package com.utesocial.android.feature_change_password.domain.use_case

import com.utesocial.android.core.domain.util.Constants
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_change_password.domain.model.ChangePasswordReq
import com.utesocial.android.feature_change_password.domain.repository.ChangePasswordRepository
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ChangePasswordUseCase(private val changePasswordRepository: ChangePasswordRepository) {

//    operator fun invoke(changePasswordReq: ChangePasswordReq): Flow<Resource<String>> = flow {
//        try {
//            emit(Resource.Loading())
//            val responseDto = changePasswordRepository.changePassword(changePasswordReq)
//            val message = responseDto.message
//
//            if (responseDto.status == Constants.RESPONSE_SUCCESS) {
//                emit(Resource.Success(data = message))
//            } else {
//                emit(Resource.Error(message = message))
//            }
//        }  catch (error: HttpException) {
//            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
//        } catch (error: IOException) {
//            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
//        }
//    }
    operator fun invoke(changePasswordReq: ChangePasswordReq): SimpleCall<AppResponse<Void>> {
        return changePasswordRepository.changePassword(changePasswordReq)
    }
}