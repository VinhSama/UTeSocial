package com.utesocial.android.core.data.network.dto

data class ResponseDto<O>(
    val message: String,
    val status: Int,
    val data: O
)