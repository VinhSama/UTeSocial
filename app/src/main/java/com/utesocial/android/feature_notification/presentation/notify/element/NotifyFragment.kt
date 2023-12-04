package com.utesocial.android.feature_notification.presentation.notify.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentNotifyBinding
import com.utesocial.android.feature_notification.presentation.notify.adapter.NotifyAdapter
import com.utesocial.android.feature_notification.presentation.notify.element.partial.NotifyBottomSheet
import com.utesocial.android.feature_notification.presentation.notify.listener.NotifyItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotifyFragment : BaseFragment() {

    private lateinit var binding: FragmentNotifyBinding

    private val notifyBottomSheet by lazy { NotifyBottomSheet() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notify, container, false)
        return binding
    }

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@NotifyFragment }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerFrameLayout.startShimmer()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = GONE
                binding.swipeRefreshLayout.visibility = VISIBLE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                val aaa = arrayOf(true, true, false, true, false, false, false, true, false, false, false, true)
                val adapter = NotifyAdapter(aaa, notifyItemListener)
                withContext(Dispatchers.Main) { binding.recyclerViewNotify.adapter = adapter }

                delay(2000)
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.textViewEmpty.visibility = GONE
                    binding.recyclerViewNotify.visibility = VISIBLE
                }
            }
        }
    }

    private val notifyItemListener = object : NotifyItemListener {

        override fun onLongClick() {
            notifyBottomSheet.show(getBaseActivity().supportFragmentManager, NotifyBottomSheet.TAG)
        }
    }
}