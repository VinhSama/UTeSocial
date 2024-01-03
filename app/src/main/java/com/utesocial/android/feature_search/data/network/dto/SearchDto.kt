package com.utesocial.android.feature_search.data.network.dto

import com.utesocial.android.feature_search.domain.model.SearchUser

data class SearchDto(
    val users: List<SearchUser> = emptyList(),
    val totalCount: Int = 0
)