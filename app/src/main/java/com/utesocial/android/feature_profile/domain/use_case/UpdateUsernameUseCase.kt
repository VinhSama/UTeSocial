package com.utesocial.android.feature_profile.domain.use_case

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class UpdateUsernameUseCase(private val profileRepository: ProfileRepository) {

    operator fun invoke(username: UsernameReq): SimpleCall<AppResponse<User>> =
        profileRepository.updateUsername(username)
}