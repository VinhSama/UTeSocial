package com.utesocial.android.feature_notification.domain.use_case

import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_notification.domain.model.Notify
import com.utesocial.android.feature_notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetNotifiesUseCase(private val notificationRepository: NotificationRepository) {

    operator fun invoke(): Flow<Resource<List<Notify>>> = flow {
        try {
            emit(Resource.Loading())

            val notifies = notificationRepository.getNotifies().map { it.toNotify() }
            emit(Resource.Success(notifies))
        } catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}