package com.utesocial.android.feature_notification.presentation.notify.state_holder.state

import com.utesocial.android.feature_notification.domain.model.Notify

data class NotifyState(
    val isLoading: Boolean = false,
    val error: String = "",
    val notifies: List<Notify> = emptyList()
)