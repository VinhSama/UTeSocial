package com.utesocial.android.core.presentation.main.element

import android.animation.AnimatorInflater
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseActivity
import com.utesocial.android.core.presentation.main.element.partial.MainActivityBottom
import com.utesocial.android.core.presentation.main.element.partial.MainActivityScreen
import com.utesocial.android.core.presentation.main.element.partial.MainActivitySearch
import com.utesocial.android.core.presentation.main.element.partial.MainActivityTop
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.NavigationUICustom
import com.utesocial.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main) }
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    private val bottomBinding by lazy { MainActivityBottom(this@MainActivity, binding.bottomBar) }
    private val screenBinding by lazy { MainActivityScreen(this@MainActivity, binding.screen) }
    private val searchBinding by lazy { MainActivitySearch(this@MainActivity, binding.search) }
    private val topBinding by lazy { MainActivityTop(this@MainActivity, binding.topBar) }

    override fun initDataBinding(): ViewDataBinding = binding

    override fun initViewModel(): ViewModel = viewModel

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@MainActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setSplashScreenExit(splashScreen)
        setup(splashScreen)
        setupListener()
    }

    private fun setSplashScreenExit(splashScreen: SplashScreen) = splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
        AnimatorInflater.loadAnimator(this@MainActivity, R.animator.animator_act_main_splash_screen_exit).apply {
            setTarget(splashScreenViewProvider.view)
            doOnEnd { splashScreenViewProvider.remove() }
            start()
        }
    }

    private fun setup(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition { true }

        disableDragActionBar(topBinding.appBarLayout())
        setupActionBar(topBinding.relativeLayoutAction(), screenBinding.frameLayoutScreen())
        setupBottomBar(bottomBinding.bottomAppBar(), bottomBinding.bottomViewOnScrollBehavior())

        NavigationUICustom.setupWithNavController(
            this@MainActivity,
            bottomBinding.bottomNavigationView(),
            screenBinding.navController(),
            animEnter = R.anim.anim_act_main_bnv_enter,
            animExit = R.anim.anim_act_main_bnv_exit,
            animPopEnter = R.anim.anim_act_main_bnv_enter,
            animPopExit = R.anim.anim_act_main_bnv_exit,
            animatorEnter = R.animator.animator_act_main_bnv_enter,
            animatorExit = R.animator.animator_act_main_bnv_exit,
            animatorPopEnter = R.animator.animator_act_main_bnv_enter,
            animatorPopExit = R.animator.animator_act_main_bnv_exit,
            R.id.item_fra_notification, R.id.item_fra_settings
        )

        topBinding.setup()
        bottomBinding.setup()

        splashScreen.setKeepOnScreenCondition { false }
    }

    private fun setupListener() {
        topBinding.setListener(searchBinding.searchView())
    }
}