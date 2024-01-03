package com.utesocial.android.feature_search.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchUserDto
import com.utesocial.android.feature_search.domain.repository.SearchUserRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class SearchUserUseCase(private val searchUserRepository: SearchUserRepository) {

    operator fun invoke(page: Int, limit: Int, search: String = ""): SimpleCall<AppResponse<SearchUserDto>> =
        searchUserRepository.searchUsers(page, limit, search)
}