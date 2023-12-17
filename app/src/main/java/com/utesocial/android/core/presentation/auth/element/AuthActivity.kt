package com.utesocial.android.core.presentation.auth.element

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseActivity
import com.utesocial.android.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override val binding: ActivityAuthBinding by lazy { DataBindingUtil.setContentView(this@AuthActivity, R.layout.activity_auth) }
    override val viewModel = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        setupNavController(navController)
    }
}