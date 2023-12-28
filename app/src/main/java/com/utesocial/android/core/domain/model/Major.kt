package com.utesocial.android.core.domain.model

import java.io.Serializable

data class Major(
    val code: String?,
    val name: HashMap<String, String>
) : Serializable {
    override fun toString(): String {
        return "Major(code=$code, name=$name)"
    }
}