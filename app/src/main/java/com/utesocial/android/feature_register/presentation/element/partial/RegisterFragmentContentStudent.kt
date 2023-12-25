package com.utesocial.android.feature_register.presentation.element.partial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
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
import com.utesocial.android.databinding.FragmentRegisterContentStudentBinding
import com.utesocial.android.feature_register.domain.model.EnrollmentYearRes
import com.utesocial.android.feature_register.domain.model.FacultyRes
import com.utesocial.android.feature_register.domain.model.MajorRes
import com.utesocial.android.feature_register.presentation.adapter.RegisterEnrollmentYearAdapter
import com.utesocial.android.feature_register.presentation.adapter.RegisterFacultyAdapter
import com.utesocial.android.feature_register.presentation.adapter.RegisterMajorAdapter
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterFragmentContentStudent : BaseFragment<FragmentRegisterContentStudentBinding>() {

    override lateinit var binding: FragmentRegisterContentStudentBinding
    override val viewModel: RegisterViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })

    private lateinit var chooseFaculty: FacultyRes
    private lateinit var facultyDialogChoose: DialogChoose
    private val registerFacultyData: ArrayList<FacultyRes> by lazy { ArrayList() }
    private val registerFacultyAdapter: RegisterFacultyAdapter by lazy {
        RegisterFacultyAdapter(
            this@RegisterFragmentContentStudent,
            registerFacultyData,
            facultyDialogChoose.binding.recyclerViewList)
    }

    private lateinit var chooseMajor: MajorRes
    private lateinit var majorDialogChoose: DialogChoose
    private val registerMajorData: ArrayList<MajorRes> by lazy { ArrayList() }
    private val registerMajorAdapter: RegisterMajorAdapter by lazy {
        RegisterMajorAdapter(
            this@RegisterFragmentContentStudent,
            registerMajorData,
            majorDialogChoose.binding.recyclerViewList
        )
    }

    private lateinit var chooseEnrollmentYear: EnrollmentYearRes
    private lateinit var enrollmentYearDialogChoose: DialogChoose
    private val registerEnrollmentYearData: ArrayList<EnrollmentYearRes> by lazy { ArrayList() }
    private val registerEnrollmentYearAdapter: RegisterEnrollmentYearAdapter by lazy {
        RegisterEnrollmentYearAdapter(
            this@RegisterFragmentContentStudent,
            registerEnrollmentYearData,
            enrollmentYearDialogChoose.binding.recyclerViewList
        )
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRegisterContentStudentBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_content_student, container, false)
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
            majorDialogChoose = DialogChoose(container)
            enrollmentYearDialogChoose = DialogChoose(container)
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

        val majorDialogData = DialogChooseData(
            title = getString(R.string.str_fra_register_dialog_major_title),
            content = getString(R.string.str_fra_register_dialog_major_content),
            positive = getString(R.string.str_fra_register_dialog_major_positive),
            neutral = getString(R.string.str_fra_register_dialog_major_neutral)
        )
        majorDialogChoose.setData(majorDialogData)

        val enrollmentYearDialogData = DialogChooseData(
            title = getString(R.string.str_fra_register_dialog_enrollment_year_title),
            content = getString(R.string.str_fra_register_dialog_enrollment_year_content),
            positive = getString(R.string.str_fra_register_dialog_enrollment_year_positive),
            neutral = getString(R.string.str_fra_register_dialog_enrollment_year_neutral)
        )
        enrollmentYearDialogChoose.setData(enrollmentYearDialogData)

        binding.textInputLayoutMajor.isEnabled = !binding.textInputEditTextMajor.text.isNullOrEmpty()
        binding.textInputLayoutEnrollment.isEnabled = !binding.textInputEditTextEnrollment.text.isNullOrEmpty()
    }

    private fun setupBinding() { binding.fragment = this@RegisterFragmentContentStudent }

    private fun setupRecyclerView() {
        facultyDialogChoose.setList(registerFacultyAdapter)
        majorDialogChoose.setList(registerMajorAdapter)
        enrollmentYearDialogChoose.setList(registerEnrollmentYearAdapter)
    }

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

            binding.textInputEditTextMajor.setText("")
            binding.textInputLayoutMajor.error = null
            binding.textInputLayoutMajor.isErrorEnabled = false
            viewModel.getMajorsByFaculty(chooseFaculty.id)

            binding.textInputEditTextFaculty.setText(chooseFaculty.name)
            facultyDialogChoose.dismissDialog()
        }
        facultyDialogChoose.setNeutralButtonClickListener { facultyDialogChoose.dismissDialog() }

        majorDialogChoose.setPositiveButtonClickListener {
            chooseMajor = registerMajorAdapter.getMajorSelect()

            binding.textInputEditTextMajor.setText(chooseMajor.name["vi"])
            majorDialogChoose.dismissDialog()
        }
        majorDialogChoose.setNeutralButtonClickListener { majorDialogChoose.dismissDialog() }

        enrollmentYearDialogChoose.setPositiveButtonClickListener {
            chooseEnrollmentYear = registerEnrollmentYearAdapter.getEnrollmentYearSelect()

            binding.textInputEditTextEnrollment.setText(chooseEnrollmentYear.name)
            enrollmentYearDialogChoose.dismissDialog()
        }
        enrollmentYearDialogChoose.setNeutralButtonClickListener { enrollmentYearDialogChoose.dismissDialog() }
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
                            viewModel.getMajorsByFaculty(chooseFaculty.id)
                        } else {
                            getBaseActivity().showSnackbar(it)
                        }
                    }
                }
            }

            launch {
                viewModel.listEnrollmentYears.collect {
                    if (it.isEmpty()) {
                        binding.textInputLayoutEnrollment.isEnabled = false
                    } else {
                        if (!binding.textInputEditTextMajor.text.isNullOrEmpty()) {
                            binding.textInputLayoutEnrollment.isEnabled = true
                        }

                        registerEnrollmentYearAdapter.resetData()
                        registerEnrollmentYearData.clear()

                        registerEnrollmentYearData.addAll(it.sortedBy { enrollmentYear -> enrollmentYear.startYear })
                        registerEnrollmentYearAdapter.notifyItemRangeChanged(0, registerEnrollmentYearData.size)
                    }
                }
            }

            launch {
                viewModel.errorEnrollmentYearRes.collect {
                    if (it.isNotEmpty()) {
                        binding.textInputEditTextEnrollment.setText("")
                        binding.textInputLayoutEnrollment.isEnabled = false
                        binding.textInputLayoutEnrollment.error = ""
                        binding.textInputLayoutEnrollment.isErrorEnabled = false

                        if (it == "Couldn't reach server. Check your internet connect") {
                            delay(2500)
                            viewModel.getEnrollmentYears()
                        } else {
                            getBaseActivity().showSnackbar(it)
                        }
                    }
                }
            }
        }
    }

    fun updateContinueButton() {
        val isIdentityValid = checkValid(binding.textInputLayoutIdentity, binding.textInputEditTextIdentity)
        val isGraduatedValid = binding.radioButtonGraduated.isChecked || binding.radioButtonNotGraduated.isChecked
        val isFacultyValid = checkValid(binding.textInputLayoutFaculty, binding.textInputEditTextFaculty)
        val isMajorValid = checkValid(binding.textInputLayoutMajor, binding.textInputEditTextMajor)
        val isEnrollmentValid = checkValid(binding.textInputLayoutEnrollment, binding.textInputEditTextEnrollment)
        val isClassValid = checkValid(binding.textInputLayoutClass, binding.textInputEditTextClass)

        val isContinue = isIdentityValid && isGraduatedValid && isFacultyValid && isMajorValid && isEnrollmentValid && isClassValid
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

    fun checkEmptyFaculty() {
        val text = binding.textInputEditTextFaculty.text ?: ""
        checkEmpty(binding.textInputLayoutFaculty, text.toString())

        if (text.trim().isEmpty()) {
            binding.textInputLayoutMajor.error = null
            binding.textInputLayoutMajor.isErrorEnabled = false
            binding.textInputLayoutMajor.isEnabled = false

            binding.textInputLayoutEnrollment.error = null
            binding.textInputLayoutEnrollment.isErrorEnabled = false
            binding.textInputLayoutEnrollment.isEnabled = false
        } else {
            binding.textInputLayoutMajor.isEnabled = true
            binding.textInputLayoutEnrollment.isEnabled = !binding.textInputEditTextMajor.text.isNullOrEmpty()
        }
    }

    fun checkEmptyMajor() {
        val text = binding.textInputEditTextMajor.text ?: ""
        checkEmpty(binding.textInputLayoutMajor, text.toString())

        if (text.trim().isEmpty()) {
            binding.textInputLayoutEnrollment.error = null
            binding.textInputLayoutEnrollment.isErrorEnabled = false
        }
        binding.textInputLayoutEnrollment.isEnabled = text.isNotEmpty()
    }

    fun showDialogFaculty() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        facultyDialogChoose.showDialog()
    }

    fun showDialogMajor() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        majorDialogChoose.showDialog()
    }

    fun showDialogEnrollmentYear() {
        val viewFocus = binding.root.findFocus()
        if (viewFocus != null) {
            getBaseActivity().handleHideKeyboard(viewFocus)
        }
        enrollmentYearDialogChoose.showDialog()
    }

    fun setInfo() {
        val identityCode = binding.textInputEditTextIdentity.text.toString()
        val graduated = binding.radioButtonGraduated.isChecked
        val facultyId = chooseFaculty.id
        val majorId = chooseMajor.id
        val enrollmentYearId = chooseEnrollmentYear.id
        val classCode = binding.textInputEditTextClass.text.toString()

        viewModel.setDataRoleStudent(identityCode, graduated, facultyId, majorId, enrollmentYearId, classCode)
    }
}