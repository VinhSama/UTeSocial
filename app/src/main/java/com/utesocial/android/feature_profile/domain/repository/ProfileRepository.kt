package com.utesocial.android.feature_profile.domain.repository

import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface ProfileRepository {

    fun updateUsername(username: UsernameReq): SimpleCall<AppResponse<User>>

    fun getMyPosts(userId: String, page: Int, limit: Int): SimpleCall<AppResponse<PostBody>>
}