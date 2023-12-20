package com.utesocial.android.feature_register.presentation.element.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterContentRoleBinding
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel

class RegisterFragmentContentRole : BaseFragment<FragmentRegisterContentRoleBinding>() {

    override lateinit var binding: FragmentRegisterContentRoleBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private lateinit var chooseView: View

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentRoleBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_role, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentRole }

    fun chooseRole(type: Int) {
        chooseView = when (type) {
            1 -> binding.buttonStudent
            2 -> binding.buttonLecturer
            else -> binding.buttonCandidate
        }
        viewModel.setTypeRole(type)
    }

    fun getChooseView(): View = chooseView
}