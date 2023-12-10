package com.utesocial.android.feature_notification.presentation.request.state_holder.state

import com.utesocial.android.feature_notification.domain.model.Request

data class RequestState(
    val isLoading: Boolean = false,
    val error: String = "",
    val requests: List<Request> = emptyList()
)