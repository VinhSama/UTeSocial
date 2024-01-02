package com.utesocial.android.core.presentation.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup.GONE
import android.view.ViewGroup.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.OnScrollStateChangedListener
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.STATE_SCROLLED_DOWN
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.STATE_SCROLLED_UP
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.utesocial.android.R
import com.utesocial.android.core.presentation.view.BottomAppBarCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    abstract val binding: DB
    protected abstract val viewModel: ViewModel?

    private val animatorDuration by lazy { resources.getInteger(R.integer.duration_200).toLong() }

    private lateinit var topBar: View
    private lateinit var bottomBar: BottomAppBarCustom

    private lateinit var vaShowTopBar: ValueAnimator
    private lateinit var vaHideTopBar: ValueAnimator

    private lateinit var vaShowBottomBar: ValueAnimator
    private lateinit var vaHideBottomBar: ValueAnimator

    private lateinit var obpTopBar: OnBackPressedCallback
    private lateinit var obpBottomBar: OnBackPressedCallback

    private var navController: NavController? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this@BaseActivity
        hideKeyboard()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun hideKeyboard() {
        binding.root.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == ACTION_UP) {
                val viewFocus = currentFocus
                if (viewFocus != null) {
                    handleHideKeyboard(viewFocus)
                }
            }
            false
        }
    }

    fun handleHideKeyboard(view: View) {
        view.clearFocus()
        val inputMethodManager = getSystemService(InputMethodManager::class.java)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun disableDragActionBar(appBarLayout: AppBarLayout) {
        val clLayoutParams = appBarLayout.layoutParams as LayoutParams
        clLayoutParams.behavior = AppBarLayout.Behavior().apply {
            setDragCallback(object : DragCallback() {

                override fun canDrag(appBarLayout: AppBarLayout): Boolean = false
            })
        }
    }

    protected fun setupActionBar(
        relativeLayout: View,
        screen: View
    ) {
        topBar = relativeLayout
        setActionBarOnBackPressed()
        setAnimatorActionBar(screen)
    }

    private fun setActionBarOnBackPressed() {
        val enabled = (topBar.visibility == GONE)
        obpTopBar = object : OnBackPressedCallback(enabled) {

            override fun handleOnBackPressed() {
                handleActionBar(true)

                isEnabled = false
                onBackPressedDispatcher.onBackPressed()

                when (navController()?.currentDestination?.id) {
                    R.id.item_fra_friend_request,
                    R.id.item_fra_settings,
                    R.id.item_fra_friends_list,
                    R.id.item_fra_profile -> handleActionBar(false)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, obpTopBar)
    }

    private fun setAnimatorActionBar(screen: View) {
        val abHeight = resources.getDimension(R.dimen.act_main_top_rl_action_height)
        val clLayoutParams = screen.layoutParams as LayoutParams

        vaShowTopBar = ValueAnimator.ofFloat(-abHeight, 0F).apply {
            duration = animatorDuration
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                topBar.translationY = animatedValue
                screen.y = abHeight + animatedValue
            }
            doOnStart {
                clLayoutParams.behavior = null
                screen.requestLayout()
                topBar.visibility = VISIBLE
            }
            doOnEnd { finishAnimatorShowTopBar(screen, clLayoutParams) }
            doOnCancel { finishAnimatorShowTopBar(screen, clLayoutParams) }
        }

        vaHideTopBar = ValueAnimator.ofFloat(0F, -abHeight).apply {
            duration = animatorDuration
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                topBar.translationY = animatedValue
                screen.y = abHeight + animatedValue
            }
            doOnEnd { finishAnimatorHideTopBar(screen) }
            doOnCancel { finishAnimatorHideTopBar(screen) }
        }
    }

    private fun finishAnimatorShowTopBar(
        screen: View,
        clLayoutParams: LayoutParams
    ) {
        clLayoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
        screen.requestLayout()
    }

    private fun finishAnimatorHideTopBar(screen: View) {
        screen.y = 0F
        topBar.visibility = GONE
        obpTopBar.isEnabled = true
    }

    private fun cancelAnimatorActionBar() {
        if (vaShowTopBar.isRunning) {
            vaShowTopBar.cancel()
        }

        if (vaHideTopBar.isRunning) {
            vaHideTopBar.cancel()
        }
    }

    protected fun setupBottomBar(
        bottomAppBarCustom: BottomAppBarCustom,
        bottomViewOnScrollBehavior: OnScrollStateChangedListener
    ) {
        bottomBar = bottomAppBarCustom
        setBottomBarOnBackPressed()
        setAnimatorBottomBar(bottomViewOnScrollBehavior)
    }

    private fun setBottomBarOnBackPressed() {
        val enabled = (bottomBar.visibility == GONE)
        obpBottomBar = object : OnBackPressedCallback(enabled) {

            override fun handleOnBackPressed() {
                handleBottomBar(true)

                isEnabled = false
                onBackPressedDispatcher.onBackPressed()

                when (navController()?.currentDestination?.id) {
                    R.id.item_fra_profile -> handleBottomBar(false)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, obpBottomBar)
    }

    private fun setAnimatorBottomBar(bottomViewOnScrollBehavior: OnScrollStateChangedListener) {
        val bbHeight = resources.getDimension(R.dimen.tra_act_main_bnv_height)
        val delayStateChange = resources.getInteger(R.integer.duration_130).toLong()

        vaShowBottomBar = ValueAnimator.ofFloat(bbHeight, 0F).apply {
            duration = animatorDuration
            addUpdateListener { bottomBar.translationY = it.animatedValue as Float }
            doOnStart {
                bottomBar.visibility = VISIBLE
                bottomViewOnScrollBehavior.onStateChanged(bottomBar, STATE_SCROLLED_UP)
            }
        }

        vaHideBottomBar = ValueAnimator.ofFloat(0F, bbHeight).apply {
            duration = animatorDuration
            addUpdateListener { bottomBar.translationY = it.animatedValue as Float }
            doOnStart {
                lifecycleScope.launch(Dispatchers.IO) {
                    delay(animatorDuration - delayStateChange)
                    withContext(Dispatchers.Main) {
                        if (vaHideBottomBar.isRunning) {
                            bottomViewOnScrollBehavior.onStateChanged(bottomBar, STATE_SCROLLED_DOWN)
                        }
                    }
                }
            }
            doOnEnd { finishAnimatorHideBottomBar() }
            doOnCancel { finishAnimatorHideBottomBar() }
        }
    }

    private fun finishAnimatorHideBottomBar() {
        bottomBar.visibility = GONE
        obpBottomBar.isEnabled = true
    }

    private fun cancelAnimatorBottomBar() {
        if (vaShowBottomBar.isRunning) {
            vaShowBottomBar.cancel()
        }

        if (vaHideBottomBar.isRunning) {
            vaHideBottomBar.cancel()
        }
    }

    fun handleActionBar(isShow: Boolean) {
        cancelAnimatorActionBar()
        if (isShow) {
            if (topBar.visibility == GONE) {
                vaShowTopBar.start()
            }
        } else {
            if (topBar.visibility == VISIBLE) {
                vaHideTopBar.start()
            }
        }
    }

    fun handleBottomBar(isShow: Boolean) {
        cancelAnimatorBottomBar()
        if (isShow) {
            if (bottomBar.visibility == GONE) {
                vaShowBottomBar.start()
            }
        } else {
            if (bottomBar.visibility == VISIBLE) {
                vaHideBottomBar.start()
            }
        }
    }

    fun handleBar(isShow: Boolean) {
        handleActionBar(isShow)
        handleBottomBar(isShow)
    }

    protected fun setupNavController(navController: NavController) {
        this.navController = navController
    }

    fun navController(): NavController? = navController

    protected fun setupSnackbar(view: View?) {
        snackbar = if (view != null) {
            Snackbar.make(view, "", LENGTH_LONG).setAnchorView(view)
        } else {
            Snackbar.make(binding.root, "", LENGTH_LONG)
        }
    }

    fun showSnackbar(message: String) = snackbar?.apply {
        setText(message)
        show()
    }

}