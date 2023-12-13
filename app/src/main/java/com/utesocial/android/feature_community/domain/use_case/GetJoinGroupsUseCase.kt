package com.utesocial.android.feature_community.domain.use_case

import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.domain.model.Community
import com.utesocial.android.feature_community.domain.repository.CommunityRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetJoinGroupsUseCase(private val communityRepository: CommunityRepository) {

    operator fun invoke(): Flow<Resource<List<Community>>> = flow {
        try {
            emit(Resource.Loading())

            delay(2500)
            val joinGroups: ArrayList<Community> = ArrayList()
            joinGroups.add(Community("", "Name 1"))
            joinGroups.add(Community("", "Name 2"))
            joinGroups.add(Community("", "Name 3"))
            joinGroups.add(Community("", "Name 4"))
            joinGroups.add(Community("", "Name 5"))
            joinGroups.add(Community("", "Name 6"))
            joinGroups.add(Community("", "Name 7"))

            emit(Resource.Success(joinGroups))
        } catch (error: HttpException) {
            emit(Resource.Error(message = error.localizedMessage ?: "An unexpected error occurred"))
        } catch (error: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connect"))
        }
    }
}