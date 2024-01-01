package com.utesocial.android.feature_profile.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class GetMyPostsUseCase(private val profileRepository: ProfileRepository) {

    operator fun invoke(userId: String, page: Int, limit: Int): SimpleCall<AppResponse<PostBody>> {
        return profileRepository.getMyPosts(userId, page, limit)
    }
}