package com.utesocial.android.feature_notification.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentNotifyBinding
import com.utesocial.android.feature_notification.presentation.adapter.NotifyAdapter

class NotifyFragment : BaseFragment() {

    private lateinit var binding: FragmentNotifyBinding

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

        val aaa = arrayOf("AAA", "BBB", "AAA", "BBB", "AAA", "BBB", "AAA", "BBB")

        val adapter = NotifyAdapter(aaa)
        binding.recyclerViewNotify.adapter = adapter
    }
}