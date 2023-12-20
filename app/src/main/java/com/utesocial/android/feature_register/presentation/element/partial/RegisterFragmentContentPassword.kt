package com.utesocial.android.feature_register.presentation.element.partial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterContentPasswordBinding
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel

class RegisterFragmentContentPassword : BaseFragment<FragmentRegisterContentPasswordBinding>() {

    override lateinit var binding: FragmentRegisterContentPasswordBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentPasswordBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_password, container, false)
        return binding
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBinding()
        setupListener()
    }

    private fun setup() {
        binding.textInputLayoutPassword.isEndIconVisible = false
        binding.textInputLayoutPassword.errorIconDrawable = null

        binding.textInputLayoutRepass.isEndIconVisible = false
        binding.textInputLayoutRepass.errorIconDrawable = null
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentPassword }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        parentFragment?.view?.setOnTouchListener { _, _ ->
            val viewFocus = binding.root.findFocus()
            if (viewFocus != null) {
                getBaseActivity().handleHideKeyboard(viewFocus)
            }
            false
        }
    }

    private fun updateContinueButton() {
        val isPasswordValid = checkValid(binding.textInputLayoutPassword, binding.textInputEditTextPassword)
        val isRepassValid = checkValid(binding.textInputLayoutRepass, binding.textInputEditTextRepass)

        val isContinue = isPasswordValid && isRepassValid
        viewModel.continueNextStep(isContinue)
    }

    private fun checkValid(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ): Boolean = textInputLayout.error.isNullOrEmpty() && !textInputEditText.text.isNullOrEmpty()

    private fun setError(
        textInputLayout: TextInputLayout,
        error: String
    ) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = error

        viewModel.continueNextStep(false)
    }

    fun checkPassword() {
        binding.textInputLayoutPassword.error = null
        binding.textInputLayoutPassword.isErrorEnabled = false
        val text = binding.textInputEditTextPassword.text ?: ""

        if (text.isEmpty()) {
            binding.textInputLayoutPassword.isEndIconVisible = false
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(binding.textInputLayoutPassword, error)
        } else {
            if (!binding.textInputLayoutPassword.isEndIconVisible) {
                binding.textInputLayoutPassword.isEndIconVisible = true
            }

            if (text.length < 8) {
                val error = resources.getString(R.string.str_fra_register_error_pass_size)
                setError(binding.textInputLayoutPassword, error)
                return
            }

            if (!text.contains("[0-9]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_digit)
                setError(binding.textInputLayoutPassword, error)
                return
            }

            if (!text.contains("[a-z]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_lower)
                setError(binding.textInputLayoutPassword, error)
                return
            }

            if (!text.contains("[A-Z]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_upper)
                setError(binding.textInputLayoutPassword, error)
                return
            }

            if (!text.contains("[@!#\$%^&+=]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_special)
                setError(binding.textInputLayoutPassword, error)
                return
            }

            if (!binding.textInputEditTextRepass.text.isNullOrEmpty()) {
                checkRepass()
            }
            updateContinueButton()
        }
    }

    fun checkRepass() {
        binding.textInputLayoutRepass.error = null
        binding.textInputLayoutRepass.isErrorEnabled = false

        val password = binding.textInputEditTextPassword.text.toString()
        val repass = binding.textInputEditTextRepass.text.toString()

        if (repass.isEmpty()) {
            binding.textInputLayoutRepass.isEndIconVisible = false
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(binding.textInputLayoutRepass, error)
        } else {
            if (!binding.textInputLayoutRepass.isEndIconVisible) {
                binding.textInputLayoutRepass.isEndIconVisible = true
            }

            if (password != repass) {
                val error = resources.getString(R.string.str_fra_register_error_pass_match)
                setError(binding.textInputLayoutRepass, error)
                return
            }

            updateContinueButton()
        }
    }

    fun setInfo() {
        val password = binding.textInputEditTextPassword.text.toString()
        viewModel.setPassword(password)
    }
}