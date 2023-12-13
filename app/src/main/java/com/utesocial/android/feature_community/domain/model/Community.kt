package com.utesocial.android.feature_community.domain.model

import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post

data class Community(
    val groups: List<Group>,
    val posts: List<Post>
)