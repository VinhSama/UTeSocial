package com.utesocial.android.feature_search.domain.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchUserDto
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface SearchUserRepository {

    fun searchUsers(page: Int, limit: Int, search: String): SimpleCall<AppResponse<SearchUserDto>>
}