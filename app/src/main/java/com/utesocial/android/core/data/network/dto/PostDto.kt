package com.utesocial.android.core.data.network.dto

import com.utesocial.android.core.domain.model.Post

data class PostDto(
    val content: String,
    val date: String,
    val groupImage: String,
    val groupName: String,
    val images: List<String>,
    val isPersonal: Boolean,
    val numberComment: Int,
    val numberLike: Int,
    val ownerAvatar: String,
    val ownerName: String
) {

    fun toPost(): Post = Post(
        content = content,
        date = date,
        groupImage = groupImage,
        groupName = groupName,
        images = images,
        isPersonal = isPersonal,
        numberComment = numberComment,
        numberLike = numberLike,
        ownerAvatar = ownerAvatar,
        ownerName = ownerName
    )
}