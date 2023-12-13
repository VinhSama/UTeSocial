package com.utesocial.android.feature_community.data.network.dto

import com.utesocial.android.feature_community.domain.model.Community

data class CommunityDto(
    val image: String,
    val name: String
) {

    fun toCommunity(): Community = Community(
        image = image,
        name = name
    )
}