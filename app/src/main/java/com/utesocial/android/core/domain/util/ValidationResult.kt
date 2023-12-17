package com.utesocial.android.core.domain.util

import com.utesocial.android.R
import java.util.regex.Pattern

open class ValidationResult(val errorType: ErrorType?) {
    enum class ErrorType(val stringResourceId: Int) {
        INITIAL(0),
        EMPTY(R.string.validate_msg_field_not_empty),
        EMAIL_INVALID(R.string.validate_msg_field_email_invalid),
        PASSWORD_LESS_THAN_MINIMUM(R.string.validate_msg_pwd_field_min_length),
        PASSWORD_GREATER_THAN_MAXIMUM(R.string.validate_msg_field_max_length_256),
    }

    companion object {
        fun validateTextField(input: String?) : ErrorType? {
            if(input == null || input.trim().isEmpty()) {
                return ErrorType.EMPTY
            }
            return null
        }
        fun validateEmail(input: String?) : ErrorType? {
            val errorType: ErrorType? = validateTextField(input)
            if(errorType == null) {
                val isValid = input?.let {
                    Pattern.compile("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$")
                        .matcher(it)
                        .matches()
                } ?: false
                return if(!isValid) {
                    ErrorType.EMAIL_INVALID
                } else {
                    null
                }
            }
           return errorType

        }
        fun validatePassword(input: String?) : ErrorType? {
            var errorType: ErrorType? = null
            if (input != null) {
                if (input.trim().isEmpty()) {
                    errorType = ErrorType.EMPTY
                } else if (input.length < 8) {
                    errorType = ErrorType.PASSWORD_LESS_THAN_MINIMUM
                } else if (input.length > 256) {
                    errorType = ErrorType.PASSWORD_GREATER_THAN_MAXIMUM
                }
            }
            return errorType
        }
    }

}