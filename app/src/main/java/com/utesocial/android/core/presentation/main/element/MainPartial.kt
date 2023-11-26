package com.utesocial.android.core.presentation.main.element

import androidx.databinding.ViewDataBinding

abstract class MainPartial(
    activity: MainActivity,
    partialBinding: ViewDataBinding
) {

    init { partialBinding.lifecycleOwner = activity }
}