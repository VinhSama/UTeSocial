package com.utesocial.android.feature_notification.presentation.notify.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentNotifyBinding
import com.utesocial.android.feature_notification.domain.model.Notify
import com.utesocial.android.feature_notification.presentation.notification.state_holder.NotificationViewModel
import com.utesocial.android.feature_notification.presentation.notify.adapter.NotifyAdapter
import com.utesocial.android.feature_notification.presentation.notify.element.partial.NotifyBottomSheet
import com.utesocial.android.feature_notification.presentation.notify.listener.NotifyItemListener
import kotlinx.coroutines.launch

class NotifyFragment : BaseFragment<FragmentNotifyBinding>() {

    override lateinit var binding: FragmentNotifyBinding
    override val viewModel: NotificationViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val notifyBottomSheet by lazy { NotifyBottomSheet() }

    private val data: ArrayList<Notify> by lazy { ArrayList() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNotifyBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notify, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        val notifyAdapter = NotifyAdapter(this@NotifyFragment,data, object : NotifyItemListener {

            override fun showBottomSheet(notify: Notify): Boolean {
                notifyBottomSheet.bindNotify(notify)
                notifyBottomSheet.show(getBaseActivity().supportFragmentManager, NotifyBottomSheet.TAG)

                return notifyBottomSheet.isVisible
            }
        })
        binding.recyclerViewNotify.adapter = notifyAdapter
    }

    private fun setupListener() = binding.swipeRefreshLayout.setOnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getNotifies()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.notifyState.collect {
                if (!it.isLoading) {
                    data.clear()

                    if (it.notifies.isNotEmpty()) {
                        data.addAll(it.notifies)
                    } else if (it.error.isNotEmpty()) {
                        getBaseActivity().showSnackbar(message = it.error)
                    }
                }
            }
        }
    }
}