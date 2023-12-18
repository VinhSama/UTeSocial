package com.utesocial.android.feature_register.presentation.element.partial

import androidx.databinding.ViewDataBinding
import com.utesocial.android.feature_register.presentation.element.RegisterFragment

abstract class RegisterPartial(
    fragment: RegisterFragment,
    partialBinding: ViewDataBinding
) {

    init { partialBinding.lifecycleOwner = fragment }
}