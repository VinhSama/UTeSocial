package com.utesocial.android.feature_notification.domain.use_case

import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_notification.domain.model.Request
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetRequestsUseCase(private val notificationRepository: NotificationRepository) {

    operator fun invoke(): Flow<Resource<List<Request>>> = flow {
        try {
            emit(Resource.Loading())

            val requests = notificationRepository.getRequests().map { it.toRequest() }
            emit(Resource.Success(requests))
        } catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}