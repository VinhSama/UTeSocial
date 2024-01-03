package com.utesocial.android.feature_search.data.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.SearchApi
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.feature_search.domain.repository.SearchRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SearchRepositoryImpl(private val searchApi: SearchApi) : SearchRepository {

    override fun searchUsers(
        search: String,
        page: Int,
        limit: Int
    ): SimpleCall<AppResponse<SearchDto>> = searchApi.searchUsers(search, page, limit)
}