package com.utesocial.android.feature_login.domain.use_case

import com.utesocial.android.core.domain.util.ValidationResult

class ValidatePassword(errorType: ErrorType?) : ValidationResult(errorType) {
    companion object {
        fun validate(input: String?): ValidatePassword {
            return ValidatePassword(validatePassword(input))
        }
    }
}