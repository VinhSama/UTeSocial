package com.utesocial.android.feature_notification.presentation.notify.listener

import com.utesocial.android.feature_notification.domain.model.Notify

interface NotifyItemListener {

    fun showBottomSheet(notify: Notify): Boolean
}