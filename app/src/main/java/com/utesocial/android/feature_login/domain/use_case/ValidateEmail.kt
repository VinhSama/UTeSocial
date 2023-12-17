package com.utesocial.android.feature_login.domain.use_case

import com.utesocial.android.core.domain.util.ValidationResult

class ValidateEmail(errorType: ErrorType?) : ValidationResult(errorType) {
    companion object {
        fun validate(input: String?): ValidateEmail {
            return ValidateEmail(validateEmail(input))
        }
    }
}