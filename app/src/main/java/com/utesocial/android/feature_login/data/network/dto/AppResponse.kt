package com.utesocial.android.feature_login.data.network.dto

data class AppResponse<T>(
    val message: String,
    val status: Int,
    val data: T
) {
    override fun toString(): String {
        return "AppResponse(message='$message', status=$status, data=$data)"
    }
}