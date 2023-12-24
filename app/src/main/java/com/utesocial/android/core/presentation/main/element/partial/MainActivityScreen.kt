package com.utesocial.android.core.presentation.main.element.partial

import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.databinding.ActivityMainScreenBinding

class MainActivityScreen(
    val activity: MainActivity,
    private val binding: ActivityMainScreenBinding
) : MainPartial(activity, binding) {

    fun frameLayoutScreen(): FrameLayout = binding.frameLayoutScreen

    fun navController(): NavController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController

}