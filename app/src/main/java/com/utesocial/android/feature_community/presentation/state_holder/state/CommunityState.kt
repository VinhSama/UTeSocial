package com.utesocial.android.feature_community.presentation.state_holder.state

import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post

data class CommunityState(
    val isLoading: Boolean = false,
    val error: String = "",
    val groups: List<Group> = emptyList(),
    val posts: List<Post> = emptyList()
)