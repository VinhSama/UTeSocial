package com.utesocial.android.feature_search.data.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.SearchUserApi
import com.utesocial.android.feature_search.data.network.dto.SearchUserDto
import com.utesocial.android.feature_search.domain.repository.SearchUserRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SearchUserRepositoryImpl(private val searchUserApi: SearchUserApi) : SearchUserRepository {

    override fun searchUsers(
        page: Int,
        limit: Int,
        search: String
    ): SimpleCall<AppResponse<SearchUserDto>> = searchUserApi.searchUsers(page, limit, search)
}