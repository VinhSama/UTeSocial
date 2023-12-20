package com.utesocial.android.feature_register.domain.use_case

import com.utesocial.android.core.domain.util.Constants
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.domain.model.MajorRes
import com.utesocial.android.feature_register.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetMajorsByFacultyUseCase(private val registerRepository: RegisterRepository) {

    operator fun invoke(facultyId: String): Flow<Resource<List<MajorRes>>> = flow {
        try {
            emit(Resource.Loading())
            val responseDto = registerRepository.getMajorsByFaculty(facultyId)
            if (responseDto.status == Constants.RESPONSE_SUCCESS) {
                val listMajorRes = responseDto.data.toListMajorRes()
                emit(Resource.Success(data = listMajorRes))
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