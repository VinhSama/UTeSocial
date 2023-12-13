package com.utesocial.android.feature_community.data.network.dto

import com.utesocial.android.feature_community.domain.model.Community
import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post

data class CommunityDto(
    val groups: List<Group>,
    val posts: List<Post>
) {

    fun toCommunity(): Community = Community(
        groups = groups,
        posts = posts
    )
}