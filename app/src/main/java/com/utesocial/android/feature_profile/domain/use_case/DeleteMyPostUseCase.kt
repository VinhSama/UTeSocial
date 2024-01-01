package com.utesocial.android.feature_profile.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class DeleteMyPostUseCase(private val profileRepository: ProfileRepository) {

    operator fun invoke(postId: String): SimpleCall<AppResponse<Void>> =
        profileRepository.deleteMyPost(postId)
}