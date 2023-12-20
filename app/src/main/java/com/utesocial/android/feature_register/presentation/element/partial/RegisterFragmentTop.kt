package com.utesocial.android.feature_register.presentation.element.partial

import androidx.core.content.ContextCompat
import com.utesocial.android.R
import com.utesocial.android.databinding.FragmentRegisterTopBinding
import com.utesocial.android.feature_register.presentation.element.RegisterFragment
import com.utesocial.android.feature_register.presentation.util.RegisterStep

class RegisterFragmentTop(
    fragment: RegisterFragment,
    private val binding: FragmentRegisterTopBinding
) : RegisterPartial(fragment, binding) {

    private val context = binding.root.context
    private val itemContinue = binding.toolbar.menu.findItem(R.id.item_continue)

    init {
        binding.toolbar.setOnMenuItemClickListener {
            if (it == itemContinue) {
                fragment.nextStep()
                return@setOnMenuItemClickListener true
            }

            return@setOnMenuItemClickListener false
        }

        binding.toolbar.setNavigationOnClickListener { fragment.getBaseActivity().onBackPressedDispatcher.onBackPressed() }
    }

    fun behaviorNavigationButton(isBack: Boolean) {
        binding.toolbar.navigationIcon = if (isBack) {
            ContextCompat.getDrawable(context, R.drawable.ico_arrow_left_regular)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ico_xmark_regular)
        }
    }

    fun behaviorIconItemContinue(registerStep: RegisterStep) {
        when (registerStep) {
            RegisterStep.CHOOSE_ROLE -> itemContinue.isVisible = false

            RegisterStep.SET_PASSWORD -> {
                itemContinue.isVisible = true
                itemContinue.icon = ContextCompat.getDrawable(context, R.drawable.ico_check_regular)
            }

            else -> {
                itemContinue.isVisible = true
                itemContinue.icon = ContextCompat.getDrawable(context, R.drawable.ico_arrow_right_regular)
            }
        }
    }

    fun enableItemContinue(isEnable: Boolean) { itemContinue.isEnabled = isEnable }

    fun disableNavigationIcon() { binding.toolbar.navigationIcon = null }

    fun enableNavigationIcon() { binding.toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ico_arrow_left_regular) }
}