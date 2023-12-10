package com.utesocial.android.feature_notification.presentation.notification.element.partial

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.utesocial.android.R
import com.utesocial.android.databinding.FragmentNotificationBinding
import com.utesocial.android.databinding.ViewTabNotificationBinding

class NotificationTabItem(
    notificationBinding: FragmentNotificationBinding,
    title: String
) {

    private val binding: ViewTabNotificationBinding

    init {
        val tabLayout = notificationBinding.tabLayout
        binding = DataBindingUtil.inflate(LayoutInflater.from(tabLayout.context), R.layout.view_tab_notification, tabLayout, false)
        setupBinding(notificationBinding, title)
    }

    private fun setupBinding(
        notificationBinding: FragmentNotificationBinding,
        title: String
    ) {
        binding.lifecycleOwner = notificationBinding.lifecycleOwner

        binding.lifecycle = notificationBinding.lifecycleOwner
        binding.title = title
        binding.numberBadge = 0
    }

    fun rootView(): View = binding.root

    fun setNumberBadge(number: Int) { binding.numberBadge = number }
}