package com.utesocial.android.feature_login.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentLoginBinding
import com.utesocial.android.feature_login.presentation.state_holder.LoginViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override lateinit var binding: FragmentLoginBinding
    override val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.buttonRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginRegister()
            getBaseActivity().navController()?.navigate(action)
        }
    }
}