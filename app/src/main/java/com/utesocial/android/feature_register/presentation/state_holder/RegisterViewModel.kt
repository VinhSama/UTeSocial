package com.utesocial.android.feature_register.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.feature_register.domain.model.RegisterReq
import com.utesocial.android.feature_register.presentation.state_holder.state.RegisterState
import com.utesocial.android.feature_register.presentation.util.RegisterStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(

) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = this[APPLICATION_KEY] as UteSocial
                RegisterViewModel()
            }
        }
    }

    private var registerRequest = RegisterReq()

    private val _registerStep = MutableStateFlow(RegisterStep.ENTER_INFO)
    val registerStep: StateFlow<RegisterStep> = _registerStep

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    fun canContinueNextStep(isContinue: Boolean) = viewModelScope.launch { _registerState.value = RegisterState(isContinue = isContinue) }

    fun startLoading() = viewModelScope.launch { _registerState.value = RegisterState(isLoading = true) }

    fun setGeneralInfo(
        firstName: String,
        lastName: String,
        email: String,
        birthDate: String,
        homeTown: String
    ) = viewModelScope.launch {
        registerRequest.firstName = firstName
        registerRequest.lastName = lastName
        registerRequest.email = email
        registerRequest.birthDate = birthDate
        registerRequest.homeTown = homeTown

        _registerStep.value = RegisterStep.CHOOSE_ROLE
    }
}