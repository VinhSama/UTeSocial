package com.utesocial.android.feature_register.presentation.element.partial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.view.DialogChoose
import com.utesocial.android.core.presentation.view.DialogChooseData
import com.utesocial.android.databinding.FragmentRegisterContentCandidateBinding
import com.utesocial.android.feature_register.domain.model.FacultyRes
import com.utesocial.android.feature_register.domain.model.MajorRes
import com.utesocial.android.feature_register.presentation.adapter.RegisterFacultyAdapter
import com.utesocial.android.feature_register.presentation.adapter.RegisterMajorAdapter
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFragmentContentCandidate : BaseFragment<FragmentRegisterContentCandidateBinding>() {

    override lateinit var binding: FragmentRegisterContentCandidateBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private lateinit var chooseMajor: MajorRes
    private lateinit var majorDialogChoose: DialogChoose
    private val registerMajorData: ArrayList<MajorRes> by lazy { ArrayList() }
    private val registerMajorAdapter: RegisterMajorAdapter by lazy {
        RegisterMajorAdapter(
            this@RegisterFragmentContentCandidate,
            registerMajorData,
            majorDialogChoose.binding.recyclerViewList
        )
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentCandidateBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_candidate, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()

        if (container != null) {
            majorDialogChoose = DialogChoose(container)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBinding()
        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun setup() {
        val majorDialogData = DialogChooseData(
            title = getString(R.string.str_fra_register_dialog_major_title),
            content = getString(R.string.str_fra_register_dialog_major_content),
            positive = getString(R.string.str_fra_register_dialog_major_positive),
            neutral = getString(R.string.str_fra_register_dialog_major_neutral)
        )
        majorDialogChoose.setData(majorDialogData)

        viewModel.getMajors()
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentCandidate }

    private fun setupRecyclerView() = majorDialogChoose.setList(registerMajorAdapter)

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        parentFragment?.view?.setOnTouchListener { _, _ ->
            val viewFocus = binding.root.findFocus()
            if (viewFocus != null) {
                getBaseActivity().handleHideKeyboard(viewFocus)
            }
            false
        }

        majorDialogChoose.setPositiveButtonClickListener {
            chooseMajor = registerMajorAdapter.getMajorSelect()

            binding.textInputEditTextMajor.setText(chooseMajor.name["vi"])
            majorDialogChoose.dismissDialog()
        }
        majorDialogChoose.setNeutralButtonClickListener { majorDialogChoose.dismissDialog() }
    }

    private fun observer() = lifecycleScope.launch {
        launch {
            viewModel.listMajors.collect {
                if (it.isEmpty()) {
                    binding.textInputLayoutMajor.isEnabled = false
                } else {
                    binding.textInputLayoutMajor.isEnabled = true

                    registerMajorAdapter.resetData()
                    registerMajorData.clear()

                    registerMajorData.addAll(it.sortedBy { major -> major.name["vi"] })
                    registerMajorAdapter.notifyItemRangeChanged(0, registerMajorData.size)
                }
            }
        }

        launch {
            viewModel.errorMajorRes.collect {
                if (it.isNotEmpty()) {
                    binding.textInputEditTextMajor.setText("")
                    binding.textInputLayoutMajor.isEnabled = false
                    binding.textInputLayoutMajor.error = ""
                    binding.textInputLayoutMajor.isErrorEnabled = false

                    if (it == "Couldn't reach server. Check your internet connect") {
                        delay(2500)
                        viewModel.getMajors()
                    } else {
                        getBaseActivity().showSnackbar(it)
                    }
                }
            }
        }
    }

    private fun updateContinueButton() {
        val isIdentityValid = checkValid(binding.textInputLayoutIdentity, binding.textInputEditTextIdentity)
        val isMajorValid = checkValid(binding.textInputLayoutMajor, binding.textInputEditTextMajor)
        val isSchoolValid = checkValid(binding.textInputLayoutSchool, binding.textInputEditTextSchool)

        val isContinue = isIdentityValid && isMajorValid && isSchoolValid
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

    fun showDialogMajor() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        majorDialogChoose.showDialog()
    }

    fun setInfo() {
        val identityCode = binding.textInputEditTextIdentity.text.toString()
        val majorId = chooseMajor.id
        val highSchool = binding.textInputEditTextSchool.text.toString()

        viewModel.setDataRoleCandidate(identityCode, majorId, highSchool)
    }
}