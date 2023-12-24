package com.utesocial.android.feature_register.presentation.element.partial

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentRegisterContentInfoBinding
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.DECEMBER
import java.util.Calendar.JANUARY
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RegisterFragmentContentInfo : BaseFragment<FragmentRegisterContentInfoBinding>() {

    override lateinit var binding: FragmentRegisterContentInfoBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentInfoBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_info, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBinding()
        setupListener()
    }

    private fun setup() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar[YEAR] = calendar.get(YEAR) - 16

        calendar[DAY_OF_MONTH] = 31
        calendar[MONTH] = DECEMBER
        val dayLimit = calendar.timeInMillis

        calendar[DAY_OF_MONTH] = 1
        calendar[MONTH] = JANUARY
        val daySelection = calendar.timeInMillis

        calendar.clear()
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(dayLimit))
            .build()

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.str_fra_register_content_info_til_birth_text))
            .setCalendarConstraints(calendarConstraints)
            .setSelection(daySelection)
            .build()
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentInfo }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        parentFragment?.view?.setOnTouchListener { _, _ ->
            val viewFocus = binding.root.findFocus()
            if (viewFocus != null) {
                getBaseActivity().handleHideKeyboard(viewFocus)
            }
            false
        }

        datePicker.addOnPositiveButtonClickListener {
            val birthDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
            binding.textInputEditTextBirth.setText(birthDay)
        }
    }

    private fun updateContinueButton() {
        val isFirstValid = checkValid(binding.textInputLayoutFirst, binding.textInputEditTextFirst)
        val isLastValid = checkValid(binding.textInputLayoutLast, binding.textInputEditTextLast)
        val isEmailValid = checkValid(binding.textInputLayoutEmail, binding.textInputEditTextEmail)
        val isBirthValid = checkValid(binding.textInputLayoutBirth, binding.textInputEditTextBirth)
                && (binding.textInputEditTextBirth.text.toString() != getString(R.string.str_fra_register_content_info_til_birth_default))
        val isTownValid = checkValid(binding.textInputLayoutTown, binding.textInputEditTextTown)

        val isContinue = isFirstValid && isLastValid && isEmailValid && isBirthValid && isTownValid
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

    fun checkEmpty(
        textInputLayout: TextInputLayout,
        text: String
    ) {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false

        if (text.trim().isEmpty()) {
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(textInputLayout, error)
        } else if (text.trim().length < 2) {
            val error = resources.getString(R.string.str_fra_register_error_min)
            setError(textInputLayout, error)
        } else {
            updateContinueButton()
        }
    }

    fun checkEmail(
        textInputLayout: TextInputLayout,
        text: String
    ) {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false

        if (text.trim().isEmpty()) {
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(textInputLayout, error)
        } else if (text.trim().length < 2) {
            val error = resources.getString(R.string.str_fra_register_error_min)
            setError(textInputLayout, error)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            val error = resources.getString(R.string.str_fra_register_error_email)
            setError(textInputLayout, error)
        } else {
            updateContinueButton()
        }
    }

    fun showDatePicker() {
        if (datePicker.tag == null) {
            hideKeyboard()
            datePicker.showNow(childFragmentManager, MaterialDatePicker::class.toString())
        }
    }

    fun setInfo() {
        val firstName = binding.textInputEditTextFirst.text.toString()
        val lastName = binding.textInputEditTextLast.text.toString()
        val email = binding.textInputEditTextEmail.text.toString()
        val birthdate = binding.textInputEditTextBirth.text.toString()
        val homeTown = binding.textInputEditTextTown.text.toString()

        viewModel.setGeneralInfo(firstName, lastName, email, birthdate, homeTown)
    }
}