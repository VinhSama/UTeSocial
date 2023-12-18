package com.utesocial.android.feature_register.presentation.element.partial

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.utesocial.android.databinding.FragmentRegisterScreenBinding
import com.utesocial.android.feature_register.presentation.element.RegisterFragment

class RegisterFragmentScreen(
    fragment: RegisterFragment,
    private val binding: FragmentRegisterScreenBinding
) : RegisterPartial(fragment, binding) {

    fun navHostFragment(): NavHostFragment = binding.fragmentContainerView.getFragment<NavHostFragment>()

    fun navController(): NavController = navHostFragment().navController
}