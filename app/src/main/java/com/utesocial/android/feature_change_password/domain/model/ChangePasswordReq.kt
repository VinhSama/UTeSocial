package com.utesocial.android.feature_change_password.domain.model

data class ChangePasswordReq(
    val currentPassword: String = "",
    val newPassword: String = ""
)