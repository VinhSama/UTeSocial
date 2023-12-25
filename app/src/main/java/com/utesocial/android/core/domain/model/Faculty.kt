package com.utesocial.android.core.domain.model

import java.io.Serializable

data class Faculty(
    val code: String?,
    val name: String?
) : Serializable {
    override fun toString(): String {
        return "Faculty(code=$code, name=$name)"
    }
}