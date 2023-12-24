package com.utesocial.android.feature_register.presentation.element.partial

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.transition.ChangeBounds
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.view.DialogChoose
import com.utesocial.android.core.presentation.view.DialogChooseData
import com.utesocial.android.databinding.FragmentRegisterContentLecturerBinding
import com.utesocial.android.feature_register.domain.model.FacultyRes
import com.utesocial.android.feature_register.presentation.adapter.RegisterFacultyAdapter
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFragmentContentLecturer : BaseFragment<FragmentRegisterContentLecturerBinding>() {

    override lateinit var binding: FragmentRegisterContentLecturerBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private lateinit var chooseFaculty: FacultyRes
    private lateinit var facultyDialogChoose: DialogChoose
    private val registerFacultyData: ArrayList<FacultyRes> by lazy { ArrayList() }
    private val registerFacultyAdapter: RegisterFacultyAdapter by lazy {
        RegisterFacultyAdapter(
            this@RegisterFragmentContentLecturer,
            registerFacultyData,
            facultyDialogChoose.binding.recyclerViewList)
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentLecturerBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_lecturer, container, false)
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
            facultyDialogChoose = DialogChoose(container)
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
        val facultyDialogData = DialogChooseData(
            title = getString(R.string.str_fra_register_dialog_faculty_title),
            content = getString(R.string.str_fra_register_dialog_faculty_content),
            positive = getString(R.string.str_fra_register_dialog_faculty_positive),
            neutral = getString(R.string.str_fra_register_dialog_faculty_neutral)
        )
        facultyDialogChoose.setData(facultyDialogData)
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentLecturer }

    private fun setupRecyclerView() = facultyDialogChoose.setList(registerFacultyAdapter)

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        parentFragment?.view?.setOnTouchListener { _, _ ->
            val viewFocus = binding.root.findFocus()
            if (viewFocus != null) {
                getBaseActivity().handleHideKeyboard(viewFocus)
            }
            false
        }

        facultyDialogChoose.setPositiveButtonClickListener {
            chooseFaculty = registerFacultyAdapter.getFacultySelect()

            binding.textInputEditTextFaculty.setText(chooseFaculty.name)
            facultyDialogChoose.dismissDialog()
        }
        facultyDialogChoose.setNeutralButtonClickListener { facultyDialogChoose.dismissDialog() }
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.listFacultyRes.collect {
                    if (it.isEmpty()) {
                        binding.textInputLayoutFaculty.isEnabled = false
                    } else {
                        binding.textInputLayoutFaculty.isEnabled = true

                        registerFacultyAdapter.resetData()
                        registerFacultyData.clear()

                        registerFacultyData.addAll(it.sortedBy { faculty -> faculty.name })
                        registerFacultyAdapter.notifyItemRangeChanged(0, registerFacultyData.size)
                    }
                }
            }

            launch {
                viewModel.errorFacultyRes.collect {
                    if (it.isNotEmpty()) {
                        binding.textInputEditTextFaculty.setText("")
                        binding.textInputLayoutFaculty.isEnabled = false
                        binding.textInputLayoutFaculty.error = ""
                        binding.textInputLayoutFaculty.isErrorEnabled = false

                        if (it == "Couldn't reach server. Check your internet connect") {
                            delay(2500)
                            viewModel.getFaculties()
                        } else {
                            getBaseActivity().showSnackbar(it)
                        }
                    }
                }
            }
        }
    }

    private fun updateContinueButton() {
        val isIdentityValid = checkValid(binding.textInputLayoutIdentity, binding.textInputEditTextIdentity)
        val isFacultyValid = checkValid(binding.textInputLayoutFaculty, binding.textInputEditTextFaculty)

        val isContinue = isIdentityValid && isFacultyValid
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

    fun showDialogFaculty() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        facultyDialogChoose.showDialog()
    }

    fun setInfo() {
        val identityCode = binding.textInputEditTextIdentity.text.toString()
        val facultyId = chooseFaculty.id

        viewModel.setDataRoleLecturer(identityCode, facultyId)
    }
}