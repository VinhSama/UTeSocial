package com.utesocial.android.feature_profile.data.repository

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_profile.data.network.ProfileApi
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.feature_profile.domain.repository.ProfileRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class ProfileRepositoryImpl(private val profileApi: ProfileApi) : ProfileRepository {

    override fun updateUsername(username: UsernameReq): SimpleCall<AppResponse<User>> =
        profileApi.updateUsername(username)

    override fun getMyPosts(
        userId: String,
        page: Int,
        limit: Int
    ): SimpleCall<AppResponse<PostBody>> = profileApi.getMyPosts(userId, page, limit)
}