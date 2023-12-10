package com.utesocial.android.feature_home.presentation.state_holder.state

import com.utesocial.android.core.domain.model.Post

data class HomeState(
    val isLoading: Boolean = false,
    val error: String = "",
    val posts: List<Post> = emptyList()
)