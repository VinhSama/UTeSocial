package com.utesocial.android.feature_post.presentation.listener

import com.utesocial.android.feature_post.domain.model.PostModel

interface PostListener {

    fun onShowDetail(postModel: PostModel)

    fun onDeletePost(postId: String)
}