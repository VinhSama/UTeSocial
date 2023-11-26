package com.utesocial.android.feature_notification.presentation.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.feature_notification.presentation.element.NotifyFragment
import com.utesocial.android.feature_notification.presentation.element.RequestFragment

class NotificationAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): BaseFragment = if (position == 0) {
        NotifyFragment()
    } else {
        RequestFragment()
    }
}