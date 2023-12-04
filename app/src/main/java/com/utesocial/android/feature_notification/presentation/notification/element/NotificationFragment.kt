package com.utesocial.android.feature_notification.presentation.notification.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentNotificationBinding
import com.utesocial.android.databinding.ViewTabNotificationBinding
import com.utesocial.android.feature_notification.presentation.notification.adapter.NotificationAdapter

class NotificationFragment : BaseFragment() {

    private lateinit var binding: FragmentNotificationBinding

    private lateinit var itemNotifyBinding: ViewTabNotificationBinding
    private lateinit var itemRequestBinding: ViewTabNotificationBinding

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return binding
    }

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@NotificationFragment }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        itemNotifyBinding = DataBindingUtil.inflate(LayoutInflater.from(binding.tabLayout.context), R.layout.view_tab_notification, binding.tabLayout, false)
        itemRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(binding.tabLayout.context), R.layout.view_tab_notification, binding.tabLayout, false)

        val adapter = NotificationAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val itemBinding: ViewTabNotificationBinding
            val title: String

            if (position == 0) {
                title = "Thông báo"
                itemBinding = itemNotifyBinding
            } else {
                title = "Yêu cầu"
                itemBinding = itemRequestBinding
            }

            itemBinding.title = title
            itemBinding.numberBadge = 0
            tab.customView = itemBinding.root
        }.attach()
    }
}