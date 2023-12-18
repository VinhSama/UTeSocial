package com.utesocial.android.feature_register.presentation.element.partial

import androidx.core.content.ContextCompat
import com.utesocial.android.R
import com.utesocial.android.databinding.FragmentRegisterTopBinding
import com.utesocial.android.feature_register.presentation.element.RegisterFragment
import com.utesocial.android.feature_register.presentation.util.RegisterStep

class RegisterFragmentTop(
    private val fragment: RegisterFragment,
    private val binding: FragmentRegisterTopBinding
) : RegisterPartial(fragment, binding) {

    private val itemContinue = binding.toolbar.menu.findItem(R.id.item_continue)

    init {
        binding.toolbar.setOnMenuItemClickListener {
            if (it == itemContinue) {
                fragment.nextStep()
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

    fun behaviorNavigationButton(isBack: Boolean) {
        binding.toolbar.navigationIcon = if (isBack) {
            ContextCompat.getDrawable(binding.root.context, R.drawable.ico_arrow_left_regular)
        } else {
            ContextCompat.getDrawable(binding.root.context, R.drawable.ico_xmark_regular)
        }

        binding.toolbar.setNavigationOnClickListener {
            if (isBack) {
                fragment.previousStep()
            } else {
                fragment.exitRegister()
            }
        }
    }

    fun behaviorItemContinueButton(registerStep: RegisterStep) {
        when (registerStep) {
            RegisterStep.CHOOSE_ROLE -> enableItemContinue(false)
            RegisterStep.SET_PASSWORD -> enableItemContinue(false)
            else -> {}
        }
    }

    fun enableItemContinue(isEnable: Boolean) { itemContinue.isEnabled = isEnable }
}