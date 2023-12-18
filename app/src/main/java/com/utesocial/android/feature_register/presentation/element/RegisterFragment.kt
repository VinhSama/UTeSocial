package com.utesocial.android.feature_register.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterBinding
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentInfo
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentInfoDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentScreen
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentTop
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import com.utesocial.android.feature_register.presentation.util.RegisterStep
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override lateinit var binding: FragmentRegisterBinding
    override val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    private val screenBinding by lazy { RegisterFragmentScreen(this@RegisterFragment, binding.screen) }
    private val topBinding by lazy { RegisterFragmentTop(this@RegisterFragment, binding.topBar) }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        sharedElementEnterTransition = ChangeBounds()
//        sharedElementReturnTransition = ChangeBounds()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.registerState.collect {
//                topBinding.behaviorNavigationButton(isBack = it.isBack)
//                topBinding.behaviorItemContinueButton(registerStep = it.registerStep)
                topBinding.enableItemContinue(isEnable = it.isContinue)
            }
        }
    }

    private fun stepEnterInfo() {
        val registerFragmentContentInfo = (screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentInfo)
        registerFragmentContentInfo.setInfo()

        val action = RegisterFragmentContentInfoDirections.actionInfoRole()
        screenBinding.navController().navigate(action)
    }

    fun previousStep() = screenBinding.navController().popBackStack()

    fun exitRegister() = getBaseActivity().navController()?.popBackStack()

    fun nextStep() {
        viewModel.startLoading()
//        when (viewModel.registerState.value.registerStep) {
//            RegisterStep.ENTER_INFO -> stepEnterInfo()
//            else -> {}
//        }
    }
}