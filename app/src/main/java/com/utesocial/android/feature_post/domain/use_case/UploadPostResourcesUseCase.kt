package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostResourcesBody
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import okhttp3.RequestBody

class UploadPostResourcesUseCase(private val postRepository: PostRepository) {
    operator fun invoke(attachments : RequestBody) : SimpleCall<AppResponse<PostResourcesBody>> {
        return postRepository.uploadPostResources(attachments)
    }
}