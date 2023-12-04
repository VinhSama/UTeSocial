package com.utesocial.android.core.presentation.main.element.partial

import androidx.databinding.ViewDataBinding
import com.utesocial.android.core.presentation.main.element.MainActivity

abstract class MainPartial(
    activity: MainActivity,
    partialBinding: ViewDataBinding
) {

    init { partialBinding.lifecycleOwner = activity }
}