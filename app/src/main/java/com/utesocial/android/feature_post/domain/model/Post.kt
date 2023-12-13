package com.utesocial.android.feature_post.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Post(
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
) : Parcelable