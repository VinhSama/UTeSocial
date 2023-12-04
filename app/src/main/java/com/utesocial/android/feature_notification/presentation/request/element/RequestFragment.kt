package com.utesocial.android.feature_notification.presentation.request.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRequestBinding
import com.utesocial.android.feature_notification.presentation.request.adapter.RequestAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestFragment : BaseFragment() {

    private lateinit var binding: FragmentRequestBinding

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false)
        return binding
    }

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@RequestFragment }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerFrameLayout.startShimmer()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.swipeRefreshLayout.visibility = View.VISIBLE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                val aaa = arrayOf("true", "true", "false", "true", "false", "false", "false", "true", "false", "true")
                val adapter = RequestAdapter(aaa)
                withContext(Dispatchers.Main) { binding.recyclerViewRequest.adapter = adapter }

                delay(2000)
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.textViewEmpty.visibility = View.GONE
                    binding.recyclerViewRequest.visibility = View.VISIBLE
                }
            }
        }
    }
}