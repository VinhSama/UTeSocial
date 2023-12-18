package com.utesocial.android.feature_notification.presentation.request.element

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
import com.utesocial.android.databinding.FragmentRequestBinding
import com.utesocial.android.feature_notification.domain.model.Request
import com.utesocial.android.feature_notification.presentation.notification.state_holder.NotificationViewModel
import com.utesocial.android.feature_notification.presentation.request.adapter.RequestAdapter
import kotlinx.coroutines.launch

class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    override lateinit var binding: FragmentRequestBinding
    override val viewModel: NotificationViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private val data: ArrayList<Request> by lazy { ArrayList() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRequestBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false)
        return binding
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
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
        val requestAdapter = RequestAdapter(this@RequestFragment, data)
        binding.recyclerViewRequest.adapter = requestAdapter
    }

    private fun setupListener() = binding.swipeRefreshLayout.setOnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getRequests()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.requestState.collect {
                if (!it.isLoading) {
                    data.clear()

                    if (it.requests.isNotEmpty()) {
                        data.addAll(it.requests)
                    } else if (it.error.isNotEmpty()){
                        getBaseActivity().showSnackbar(message = it.error)
                    }
                }
            }
        }
    }
}