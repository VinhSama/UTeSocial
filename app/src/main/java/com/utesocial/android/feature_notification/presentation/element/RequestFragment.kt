package com.utesocial.android.feature_notification.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRequestBinding

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
}