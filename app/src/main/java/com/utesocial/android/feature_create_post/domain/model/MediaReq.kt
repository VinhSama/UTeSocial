package com.utesocial.android.feature_create_post.domain.model

import android.net.Uri

data class MediaReq(
    val uri: Uri? = null,
    val isVideo: Boolean = false
)