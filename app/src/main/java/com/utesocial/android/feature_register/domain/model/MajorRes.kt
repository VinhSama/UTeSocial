package com.utesocial.android.feature_register.domain.model

import com.google.gson.internal.LinkedTreeMap

data class MajorRes(
    val id: String = "",
    val code: String = "",
    val name: LinkedTreeMap<String, String> = LinkedTreeMap(),
)