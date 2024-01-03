package com.utesocial.android.feature_search.data.network.dto

import com.utesocial.android.feature_search.domain.model.SearchUser

data class SearchUserDto(
    val users: List<SearchUser> = emptyList(),
    val totalCount: Int = 0
)