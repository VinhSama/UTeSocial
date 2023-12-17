package com.utesocial.android.feature_register.presentation.element.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterContentRoleBinding
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel

class RegisterFragmentContentRole : BaseFragment<FragmentRegisterContentRoleBinding>() {

    override lateinit var binding: FragmentRegisterContentRoleBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentRoleBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_role, container, false)
        return binding
    }
}