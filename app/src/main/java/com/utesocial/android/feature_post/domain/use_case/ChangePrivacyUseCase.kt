package com.utesocial.android.feature_post.domain.use_case

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.domain.repository.PostRepository
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall

class ChangePrivacyUseCase(private val postRepository: PostRepository) {

    operator fun invoke(
        postInt: String,
        privacyRequest: PrivacyRequest
    ): SimpleCall<AppResponse<PrivacyResponse>> =
        postRepository.changePrivacy(postInt, privacyRequest)
}