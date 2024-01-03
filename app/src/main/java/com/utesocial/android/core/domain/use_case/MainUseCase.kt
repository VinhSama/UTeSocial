package com.utesocial.android.core.domain.use_case

import com.utesocial.android.feature_community.domain.use_case.SendFriendRequestUseCase
import com.utesocial.android.feature_search.domain.use_case.SearchUseCase

data class MainUseCase(
    val searchUseCase: SearchUseCase,
    val sendFriendRequestUseCase: SendFriendRequestUseCase
)