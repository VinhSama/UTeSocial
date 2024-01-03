package com.utesocial.android.feature_search.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.feature_search.domain.repository.SearchRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SearchUseCase(private val searchRepository: SearchRepository) {

    operator fun invoke(search: String = "", page: Int, limit: Int): SimpleCall<AppResponse<SearchDto>> =
        searchRepository.searchUsers(search, page, limit)
}