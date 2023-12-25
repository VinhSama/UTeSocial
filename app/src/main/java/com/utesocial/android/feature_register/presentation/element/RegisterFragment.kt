package com.utesocial.android.feature_register.presentation.element

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.snackbar.Snackbar
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterBinding
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentCandidate
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentCandidateDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentInfo
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentInfoDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentLecturer
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentLecturerDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentPassword
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentRole
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentRoleDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentStudent
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentContentStudentDirections
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentScreen
import com.utesocial.android.feature_register.presentation.element.partial.RegisterFragmentTop
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import com.utesocial.android.feature_register.presentation.util.RegisterStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override lateinit var binding: FragmentRegisterBinding
    override val viewModel: RegisterViewModel by viewModels { RegisterViewModel.Factory }

    private val screenBinding by lazy { RegisterFragmentScreen(this@RegisterFragment, binding.screen) }
    private val topBinding by lazy { RegisterFragmentTop(this@RegisterFragment, binding.topBar) }

    private var currentStep = RegisterStep.ENTER_INFO
    private val onBackPressedCallbackStep = object : OnBackPressedCallback(false) {

        override fun handleOnBackPressed() {
            val backPressedDispatcher = getBaseActivity().onBackPressedDispatcher
            viewModel.previousStep()

            isEnabled = false
            backPressedDispatcher.onBackPressed()

            if (currentStep != RegisterStep.ENTER_INFO) {
                isEnabled = true
                backPressedDispatcher.addCallback(viewLifecycleOwner, this)
            }
        }
    }
    private val onBackPressedCallbackExit = object : OnBackPressedCallback(false) {

        override fun handleOnBackPressed() {
            val action = RegisterFragmentDirections.actionRegisterLogin()
            getBaseActivity().navController()?.navigate(action)
            isEnabled = false
        }
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        observer()
    }

    private fun setupBinding() { binding.viewModel = viewModel }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.registerStep.collect {
                    currentStep = it
                    navigationRegisterStep()

                    topBinding.behaviorNavigationButton(isBack = (it != RegisterStep.ENTER_INFO))
                    topBinding.behaviorIconItemContinue(registerStep = it)
                }
            }

            launch {
                viewModel.registerState.collect { topBinding.enableItemContinue(isEnable = it.isContinue) }
            }

            launch {
                viewModel.errorFacultyRes.collect {
                    if (it.isNotEmpty() && (it == "Couldn't reach server. Check your internet connect")) {
                        delay(2500)
                        viewModel.getFaculties()
                    }
                }
            }

            launch {
                viewModel.errorEnrollmentYearRes.collect {
                    if (it.isNotEmpty() && (it == "Couldn't reach server. Check your internet connect")) {
                        delay(2500)
                        viewModel.getEnrollmentYears()
                    }
                }
            }

            launch {
                viewModel.registerResponse.collect {
                    if (it.isNotEmpty()) {
                        if (it == "Registered successfully") {
                            val action = RegisterFragmentDirections.actionRegisterLogin()
                            getBaseActivity().navController()?.navigate(action)
                            getBaseActivity().showSnackbar(getString(R.string.str_registered_successfully))
                        } else {
                            onBackPressedCallbackExit.isEnabled = false
                            onBackPressedCallbackStep.isEnabled = true
                            topBinding.enableNavigationIcon()
                            if (it.contains("HTTP 409")) {
                                getBaseActivity().showSnackbar(getString(R.string.str_registered_failed_email))
                            } else {
                                getBaseActivity().showSnackbar(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigationRegisterStep() {
        val currentFragment = screenBinding.navHostFragment().childFragmentManager.fragments[0]
        when (currentStep) {
            RegisterStep.CHOOSE_ROLE -> if (currentFragment::class == RegisterFragmentContentInfo::class) {
                val action = RegisterFragmentContentInfoDirections.actionInfoRole()
                screenBinding.navController().navigate(action)
            }

            RegisterStep.ROLE_STUDENT -> if (currentFragment::class == RegisterFragmentContentRole::class) {
                val transitionName = resources.getString(R.string.tra_register_content_role_student)
                val extras = FragmentNavigatorExtras((currentFragment as RegisterFragmentContentRole).getChooseView() to transitionName)

                val action = RegisterFragmentContentRoleDirections.actionRoleStudent()
                screenBinding.navController().navigate(action.actionId, null, null, extras)
            }

            RegisterStep.ROLE_LECTURER -> if (currentFragment::class == RegisterFragmentContentRole::class) {
                val transitionName = resources.getString(R.string.tra_register_content_role_lecturer)
                val extras = FragmentNavigatorExtras((currentFragment as RegisterFragmentContentRole).getChooseView() to transitionName)

                val action = RegisterFragmentContentRoleDirections.actionRoleLecturer()
                screenBinding.navController().navigate(action.actionId, null, null, extras)
            }

            RegisterStep.ROLE_CANDIDATE -> if (currentFragment::class == RegisterFragmentContentRole::class) {
                val transitionName = resources.getString(R.string.tra_register_content_role_candidate)
                val extras = FragmentNavigatorExtras((currentFragment as RegisterFragmentContentRole).getChooseView() to transitionName)

                val action = RegisterFragmentContentRoleDirections.actionRoleCandidate()
                screenBinding.navController().navigate(action.actionId, null, null, extras)
            }

            RegisterStep.SET_PASSWORD -> when (currentFragment::class) {
                RegisterFragmentContentStudent::class -> {
                    val action = RegisterFragmentContentStudentDirections.actionStudentPassword()
                    screenBinding.navController().navigate(action)
                }

                RegisterFragmentContentLecturer::class -> {
                    val action = RegisterFragmentContentLecturerDirections.actionLecturerPassword()
                    screenBinding.navController().navigate(action)
                }

                RegisterFragmentContentCandidate::class -> {
                    val action = RegisterFragmentContentCandidateDirections.actionCandidatePassword()
                    screenBinding.navController().navigate(action)
                }
            }

            else -> onBackPressedCallbackStep.isEnabled = false
        }
    }

    private fun stepEnterInfo() {
        onBackPressedCallbackStep.isEnabled = true
        getBaseActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallbackStep)

        val registerFragmentContentInfo = screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentInfo
        registerFragmentContentInfo.setInfo()
    }

    private fun stepRoleStudent() {
        val registerFragmentContentStudent = screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentStudent
        registerFragmentContentStudent.setInfo()
    }

    private fun stepRoleLecturer() {
        val registerFragmentContentLecturer = screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentLecturer
        registerFragmentContentLecturer.setInfo()
    }

    private fun stepRoleCandidate() {
        val registerFragmentContentCandidate = screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentCandidate
        registerFragmentContentCandidate.setInfo()
    }

    private fun stepSetPassword() {
        topBinding.disableNavigationIcon()
        onBackPressedCallbackStep.isEnabled = false

        onBackPressedCallbackExit.isEnabled = true
        getBaseActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallbackExit)

        val registerFragmentContentPassword = screenBinding.navHostFragment().childFragmentManager.fragments[0] as RegisterFragmentContentPassword
        registerFragmentContentPassword.setInfo()
    }

    fun nextStep() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        viewModel.startLoading()

        when (currentStep) {
            RegisterStep.ENTER_INFO -> stepEnterInfo()
            RegisterStep.ROLE_STUDENT -> stepRoleStudent()
            RegisterStep.ROLE_LECTURER -> stepRoleLecturer()
            RegisterStep.ROLE_CANDIDATE -> stepRoleCandidate()
            RegisterStep.SET_PASSWORD -> stepSetPassword()
            else -> {}
        }
    }
}