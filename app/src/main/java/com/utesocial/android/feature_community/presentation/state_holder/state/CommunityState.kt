package com.utesocial.android.feature_community.presentation.state_holder.state

import com.utesocial.android.feature_community.domain.model.Community
import com.utesocial.android.feature_post.domain.model.Post

data class CommunityState(
    var isLoading: Boolean = false,
    var error: String = "",
    var joinGroups: List<Community> = emptyList(),
    var postsGroup: List<Post> = emptyList(),
)