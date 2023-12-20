package com.utesocial.android.feature_register.domain.use_case

import com.utesocial.android.core.domain.util.Constants
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.domain.model.EnrollmentYearRes
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetEnrollmentYearsUseCase(private val registerRepository: RegisterRepository) {

    operator fun invoke(): Flow<Resource<List<EnrollmentYearRes>>> = flow {
        try {
            emit(Resource.Loading())
            val responseDto = registerRepository.getEnrollmentYears()
            if (responseDto.status == Constants.RESPONSE_SUCCESS) {
                val listEnrollmentYearRes = responseDto.data.toListEnrollmentYear()
                emit(Resource.Success(data = listEnrollmentYearRes))
            } else {
                emit(Resource.Error(message = responseDto.message))
            }
        }  catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}