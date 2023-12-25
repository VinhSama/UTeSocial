package com.utesocial.android.feature_settings.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_register.presentation.state_holder.RegisterViewModel
import com.utesocial.android.feature_settings.domain.use_case.SettingsUseCase
import com.utesocial.android.feature_settings.presentation.state_holder.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SettingsViewModel(
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as UteSocial).appModule
                SettingsViewModel(appModule.settingsUseCase)
            }
        }
    }

    private var _responseLogout = MutableStateFlow(SettingsState())
    val responseLogout: StateFlow<SettingsState> = _responseLogout

    fun logout(accessToken: HashMap<String, String>) = settingsUseCase.logoutUseCase(accessToken).onEach { resource ->
        when (resource) {
            is Resource.Loading -> _responseLogout.value = SettingsState()
            is Resource.Success -> _responseLogout.value = SettingsState(isLogout = true, message = resource.data ?: "Logout success")
            is Resource.Error -> _responseLogout.value = SettingsState(isLogout = false, message = resource.message ?: "An unexpected error occurred")
        }
    }.launchIn(viewModelScope)

    fun resetStatus() { _responseLogout.value = SettingsState() }
}