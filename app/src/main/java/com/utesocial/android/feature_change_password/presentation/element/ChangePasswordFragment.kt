package com.utesocial.android.feature_change_password.presentation.element

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    override lateinit var binding: FragmentChangePasswordBinding
    override val viewModel: ViewModel? = null

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChangePasswordBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
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
        binding.textInputLayoutPasswordCurrent.isEndIconVisible = false
        binding.textInputLayoutPasswordCurrent.errorIconDrawable = null

        binding.textInputLayoutPasswordNew.isEndIconVisible = false
        binding.textInputLayoutPasswordNew.errorIconDrawable = null

        binding.textInputLayoutPasswordConfirm.isEndIconVisible = false
        binding.textInputLayoutPasswordConfirm.errorIconDrawable = null
    }

    private fun setupBinding() { binding.fragment = this@ChangePasswordFragment }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        binding.scrollViewContent.setOnTouchListener { _, _ ->
            val viewFocus = binding.scrollViewContent.findFocus()
            if (viewFocus != null) {
                getBaseActivity().handleHideKeyboard(viewFocus)
            }
            false
        }

        binding.toolbar.setNavigationOnClickListener { getBaseActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun updateContinueButton() {
        val isOldPasswordValid = checkValid(binding.textInputLayoutPasswordCurrent, binding.textInputEditTextPasswordCurrent)
        val isNewPasswordValid = checkValid(binding.textInputLayoutPasswordNew, binding.textInputEditTextPasswordNew)
        val isConfirmPasswordValid = checkValid(binding.textInputLayoutPasswordConfirm, binding.textInputEditTextPasswordConfirm)

        val isContinue = isOldPasswordValid && isNewPasswordValid && isConfirmPasswordValid
        binding.buttonChangePassword.isEnabled = isContinue
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

        binding.buttonChangePassword.isEnabled = false
    }

    fun checkCurrentPassword() {
        binding.textInputLayoutPasswordCurrent.error = null
        binding.textInputLayoutPasswordCurrent.isErrorEnabled = false
        val text = binding.textInputEditTextPasswordCurrent.text ?: ""

        if (text.isEmpty()) {
            binding.textInputLayoutPasswordCurrent.isEndIconVisible = false
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(binding.textInputLayoutPasswordCurrent, error)
        } else {
            if (!binding.textInputLayoutPasswordCurrent.isEndIconVisible) {
                binding.textInputLayoutPasswordCurrent.isEndIconVisible = true
            }

            updateContinueButton()
        }
    }

    fun checkNewPassword() {
        binding.textInputLayoutPasswordNew.error = null
        binding.textInputLayoutPasswordNew.isErrorEnabled = false
        val text = binding.textInputEditTextPasswordNew.text ?: ""

        if (text.isEmpty()) {
            binding.textInputLayoutPasswordNew.isEndIconVisible = false
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(binding.textInputLayoutPasswordNew, error)
        } else {
            if (!binding.textInputLayoutPasswordNew.isEndIconVisible) {
                binding.textInputLayoutPasswordNew.isEndIconVisible = true
            }

            if (text.length < 8) {
                val error = resources.getString(R.string.str_fra_register_error_pass_size)
                setError(binding.textInputLayoutPasswordNew, error)
                return
            }

            if (!text.contains("[0-9]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_digit)
                setError(binding.textInputLayoutPasswordNew, error)
                return
            }

            if (!text.contains("[a-z]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_lower)
                setError(binding.textInputLayoutPasswordNew, error)
                return
            }

            if (!text.contains("[A-Z]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_upper)
                setError(binding.textInputLayoutPasswordNew, error)
                return
            }

            if (!text.contains("[@!#\$%^&+=]".toRegex())) {
                val error = resources.getString(R.string.str_fra_register_error_pass_special)
                setError(binding.textInputLayoutPasswordNew, error)
                return
            }

            if (!binding.textInputEditTextPasswordConfirm.text.isNullOrEmpty()) {
                checkConfirmPassword()
            }

            updateContinueButton()
        }
    }

    fun checkConfirmPassword() {
        binding.textInputLayoutPasswordConfirm.error = null
        binding.textInputLayoutPasswordConfirm.isErrorEnabled = false

        val passwordNew = binding.textInputEditTextPasswordNew.text.toString()
        val passwordConfirm = binding.textInputEditTextPasswordConfirm.text.toString()

        if (passwordConfirm.isEmpty()) {
            binding.textInputLayoutPasswordConfirm.isEndIconVisible = false
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(binding.textInputLayoutPasswordConfirm, error)
        } else {
            if (!binding.textInputLayoutPasswordConfirm.isEndIconVisible) {
                binding.textInputLayoutPasswordConfirm.isEndIconVisible = true
            }

            if (passwordNew != passwordConfirm) {
                val error = resources.getString(R.string.str_fra_register_error_pass_match)
                setError(binding.textInputLayoutPasswordConfirm, error)
                return
            }

            updateContinueButton()
        }
    }
}