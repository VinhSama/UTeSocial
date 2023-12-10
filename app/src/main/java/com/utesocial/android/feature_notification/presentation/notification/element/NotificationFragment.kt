package com.utesocial.android.feature_notification.presentation.notification.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentNotificationBinding
import com.utesocial.android.feature_notification.presentation.notification.adapter.NotificationAdapter
import com.utesocial.android.feature_notification.presentation.notification.element.partial.NotificationTabItem
import com.utesocial.android.feature_notification.presentation.notification.state_holder.NotificationViewModel
import kotlinx.coroutines.launch

class NotificationFragment : BaseFragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels { NotificationViewModel.Factory }

    private lateinit var tabNotify: NotificationTabItem
    private lateinit var tabRequest: NotificationTabItem

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return binding
    }

    override fun initViewModel(): ViewModel = viewModel

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@NotificationFragment }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupTabItem()
        observer()
    }

    private fun setupTabItem() {
        tabNotify = NotificationTabItem(binding, resources.getString(R.string.str_fra_notification_tab_title_notify))
        tabRequest = NotificationTabItem(binding, resources.getString(R.string.str_fra_notification_tab_title_request))

        val notificationAdapter = NotificationAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = notificationAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabItem: NotificationTabItem = if (position == 0) {
                tabNotify
            } else {
                tabRequest
            }
            tab.customView = tabItem.rootView()
        }.attach()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.notifyBadge.collect { tabNotify.setNumberBadge(it) }
            }
            launch {
                viewModel.requestBadge.collect { tabRequest.setNumberBadge(it) }
            }
        }
    }
}