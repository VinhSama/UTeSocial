package com.utesocial.android.feature_register.domain.use_case

import com.utesocial.android.core.domain.util.Constants.RESPONSE_SUCCESS
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.domain.model.FacultyRes
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetFacultiesUseCase(private val registerRepository: RegisterRepository) {

    operator fun invoke(): Flow<Resource<List<FacultyRes>>> = flow {
        try {
            emit(Resource.Loading())
            val responseDto = registerRepository.getFaculties()
            if (responseDto.status == RESPONSE_SUCCESS) {
                val listFacultyRes = responseDto.data.toListFacultyRes()
                emit(Resource.Success(data = listFacultyRes))
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