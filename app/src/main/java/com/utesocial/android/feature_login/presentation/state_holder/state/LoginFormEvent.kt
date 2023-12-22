package com.utesocial.android.feature_login.presentation.state_holder.state

sealed class LoginFormEvent {
    data class EmailChanged(val email: String) : LoginFormEvent()
    data class PasswordChanged(val password: String) : LoginFormEvent()
    data object Submit : LoginFormEvent()
}