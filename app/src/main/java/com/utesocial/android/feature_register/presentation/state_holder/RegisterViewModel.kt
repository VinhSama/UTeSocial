package com.utesocial.android.feature_register.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.domain.model.DetailsReq
import com.utesocial.android.feature_register.domain.model.EnrollmentYearRes
import com.utesocial.android.feature_register.domain.model.FacultyRes
import com.utesocial.android.feature_register.domain.model.MajorRes
import com.utesocial.android.feature_register.domain.model.RegisterReq
import com.utesocial.android.feature_register.domain.use_case.RegisterUseCase
import com.utesocial.android.feature_register.presentation.state_holder.state.RegisterState
import com.utesocial.android.feature_register.presentation.util.RegisterStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
                RegisterViewModel(appModule.registerUseCase)
            }
        }
    }

    private var registerRequest = RegisterReq()
    private val _registerResponse = MutableStateFlow("")
    val registerResponse: StateFlow<String> = _registerResponse

    private val _registerStep = MutableStateFlow(RegisterStep.ENTER_INFO)
    val registerStep: StateFlow<RegisterStep> = _registerStep

    private val _listFaculties = MutableStateFlow<List<FacultyRes>>(emptyList())
    val listFacultyRes: StateFlow<List<FacultyRes>> = _listFaculties
    private val _errorFacultyRes = MutableStateFlow("")
    val errorFacultyRes: StateFlow<String> = _errorFacultyRes

    private val _listMajors = MutableStateFlow<List<MajorRes>>(emptyList())
    val listMajors: StateFlow<List<MajorRes>> = _listMajors
    private val _errorMajorRes = MutableStateFlow("")
    val errorMajorRes: StateFlow<String> = _errorMajorRes

    private val _listEnrollmentYears = MutableStateFlow<List<EnrollmentYearRes>>(emptyList())
    val listEnrollmentYears: StateFlow<List<EnrollmentYearRes>> = _listEnrollmentYears
    private val _errorEnrollmentYearRes = MutableStateFlow("")
    val errorEnrollmentYearRes: StateFlow<String> = _errorEnrollmentYearRes

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    init {
        getFaculties()
        getEnrollmentYears()
    }

    fun getFaculties() = registerUseCase.getFacultiesUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _listFaculties.value = emptyList()
                _errorFacultyRes.value = ""
            }
            is Resource.Success -> _listFaculties.value = resource.data ?: emptyList()
            is Resource.Error -> _errorFacultyRes.value = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)

    fun getMajorsByFaculty(facultyId: String) = registerUseCase.getMajorsByFacultyUseCase(facultyId).onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _listMajors.value = emptyList()
                _errorMajorRes.value = ""
            }
            is Resource.Success -> _listMajors.value = resource.data ?: emptyList()
            is Resource.Error -> _errorMajorRes.value = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)

    fun getMajors() = registerUseCase.getMajorsByNumberItemUseCase(200).onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _listMajors.value = emptyList()
                _errorMajorRes.value = ""
            }
            is Resource.Success -> _listMajors.value = resource.data ?: emptyList()
            is Resource.Error -> _errorMajorRes.value = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)

    fun getEnrollmentYears() = registerUseCase.getEnrollmentYearsUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _listEnrollmentYears.value = emptyList()
                _errorEnrollmentYearRes.value = ""
            }
            is Resource.Success -> _listEnrollmentYears.value = resource.data ?: emptyList()
            is Resource.Error -> _errorEnrollmentYearRes.value = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)

    private fun registerUser() = registerUseCase.registerUserUseCase(registerRequest).onEach { resource ->
        when (resource) {
            is Resource.Loading -> _registerResponse.value = ""
            is Resource.Success -> {
                _registerResponse.value = "Registered successfully"
                _registerState.value = RegisterState()
            }
            is Resource.Error -> {
                _registerResponse.value = resource.message ?: "An unexpected error occurred"
                _registerState.value = RegisterState()
            }
        }
    }.launchIn(viewModelScope)

    fun continueNextStep(isContinue: Boolean) = viewModelScope.launch { _registerState.value = RegisterState(isContinue = isContinue) }

    fun startLoading() = viewModelScope.launch { _registerState.value = RegisterState(isLoading = true) }

    fun previousStep() = viewModelScope.launch {
        _registerState.value = RegisterState()

        when (registerStep.value) {
            RegisterStep.CHOOSE_ROLE -> _registerStep.value = RegisterStep.ENTER_INFO
            RegisterStep.SET_PASSWORD -> when (registerRequest.details::class) {
                DetailsReq.StudentReq::class -> _registerStep.value = RegisterStep.ROLE_STUDENT
                DetailsReq.LecturerReq::class -> _registerStep.value = RegisterStep.ROLE_LECTURER
                DetailsReq.CandidateReq::class -> _registerStep.value = RegisterStep.ROLE_CANDIDATE
            }
            else -> _registerStep.value = RegisterStep.CHOOSE_ROLE
        }
    }

    fun setGeneralInfo(
        firstName: String,
        lastName: String,
        email: String,
        birthdate: String,
        homeTown: String
    ) = viewModelScope.launch {
        registerRequest.firstName = firstName
        registerRequest.lastName = lastName
        registerRequest.email = email
        registerRequest.birthdate = birthdate
        registerRequest.homeTown = homeTown

        _registerStep.value = RegisterStep.CHOOSE_ROLE
        _registerState.value = RegisterState()
    }

    fun setTypeRole(type: Int) = viewModelScope.launch {
        registerRequest.type = type

        when (type) {
            1 -> {
                registerRequest.details = DetailsReq.StudentReq()
                _registerStep.value = RegisterStep.ROLE_STUDENT
            }
            2 -> {
                registerRequest.details = DetailsReq.LecturerReq()
                _registerStep.value = RegisterStep.ROLE_LECTURER
            }
            else -> {
                registerRequest.details = DetailsReq.CandidateReq()
                _registerStep.value = RegisterStep.ROLE_CANDIDATE
            }
        }

        _registerState.value = RegisterState()
    }

    private fun transitionSetPassword() {
        _registerStep.value = RegisterStep.SET_PASSWORD
        _registerState.value = RegisterState()
    }

    fun setDataRoleStudent(
        identityCode: String,
        graduated: Boolean,
        facultyId: String,
        majorId: String,
        enrollmentYearId: String,
        classCode: String
    ) = viewModelScope.launch {
        registerRequest.identityCode = identityCode
        (registerRequest.details as DetailsReq.StudentReq).graduated = graduated
        (registerRequest.details as DetailsReq.StudentReq).faculty = facultyId
        (registerRequest.details as DetailsReq.StudentReq).major = majorId
        (registerRequest.details as DetailsReq.StudentReq).enrollmentYear = enrollmentYearId
        (registerRequest.details as DetailsReq.StudentReq).classCode = classCode

        transitionSetPassword()
    }

    fun setDataRoleLecturer(
        identityCode: String,
        facultyId: String
    ) = viewModelScope.launch {
        registerRequest.identityCode = identityCode
        (registerRequest.details as DetailsReq.LecturerReq).faculty = facultyId

        transitionSetPassword()
    }

    fun setDataRoleCandidate(
        identityCode: String,
        majorId: String,
        highSchool: String
    ) = viewModelScope.launch {
        registerRequest.identityCode = identityCode
        (registerRequest.details as DetailsReq.CandidateReq).registeredMajor = majorId
        (registerRequest.details as DetailsReq.CandidateReq).highSchool = highSchool

        transitionSetPassword()
    }

    fun setPassword(password: String) = viewModelScope.launch {
        registerRequest.password = password
        registerUser()
    }
}