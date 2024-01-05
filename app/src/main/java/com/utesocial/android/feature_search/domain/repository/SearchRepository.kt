package com.utesocial.android.feature_search.domain.repository

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

interface SearchRepository {

    fun searchUsers(search: String, page: Int, limit: Int): SimpleCall<AppResponse<SearchDto>>
}