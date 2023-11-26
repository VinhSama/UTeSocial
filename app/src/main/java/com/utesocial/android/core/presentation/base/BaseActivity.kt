package com.utesocial.android.core.presentation.base

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.GONE
import android.view.ViewGroup.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.OnScrollStateChangedListener
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.STATE_SCROLLED_DOWN
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.STATE_SCROLLED_UP
import com.utesocial.android.R
import com.utesocial.android.core.presentation.util.BottomAppBarCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity : AppCompatActivity() {

    private val animatorDuration by lazy { resources.getInteger(R.integer.duration_200).toLong() }

    private lateinit var topBar: View
    private lateinit var bottomBar: BottomAppBarCustom

    private lateinit var vaShowTopBar: ValueAnimator
    private lateinit var vaHideTopBar: ValueAnimator

    private lateinit var vaShowBottomBar: ValueAnimator
    private lateinit var vaHideBottomBar: ValueAnimator

    abstract fun initDataBinding(): ViewDataBinding

    abstract fun initViewModel(): ViewModel

    abstract fun assignLifecycleOwner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initViewModel()
        assignLifecycleOwner()
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
        setAnimatorActionBar(screen)
    }

    private fun setAnimatorActionBar(screen: View) {
        val abHeight = resources.getDimension(R.dimen.main_rl_action_height)
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
        setAnimatorBottomBar(bottomViewOnScrollBehavior)
    }

    private fun setAnimatorBottomBar(bottomViewOnScrollBehavior: OnScrollStateChangedListener) {
        val bbHeight = resources.getDimension(R.dimen.transition_y_bnv_height)
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
            doOnEnd { bottomBar.visibility = GONE }
            doOnCancel { bottomBar.visibility = GONE }
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

    private fun cancelAnimatorBottomBar() {
        if (vaShowBottomBar.isRunning) {
            vaShowBottomBar.cancel()
        }

        if (vaHideBottomBar.isRunning) {
            vaHideBottomBar.cancel()
        }
    }

    fun handleBar(isShow: Boolean) {
        handleActionBar(isShow)
        handleBottomBar(isShow)
    }



    /** Keyboard */
    /**
    window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
    initDataBinding().root.setOnTouchListener { view, motionEvent
        hideKeyboard()
        false
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        val inputMethodManager = getSystemService(InputMethodManager::class.java)
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            view.clearFocus()
            val inputMethodManager = getSystemService(InputMethodManager::class.java)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    */


    /** Permission Request */
    /**
    private val GRANTED: Int = 0
    private val FIRST: Int = 1
    private val DENIED: Int = 2
    private val NEVER: Int = 3

    data class Permission(
        var name: String,
        var status: Int,
        var isShow: Boolean
    )

    fun Permission.save() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun changeStatusPermission(
        permissions: List<Permission>,
        resultCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            changeStatusPermissionNew(permissions, resultCode)
        } else {
            changeStatusPermissionOld(permissions, resultCode)
        }
    }

    fun changeStatusPermissionNew(
        permissions: List<Permission>,
        resultCode: Int
    ) {
        val permissionRepresent = permissions[0]
        var result = if (resultCode == 1) checkSelfPermission(permissionRepresent.name) else resultCode

        if (result == PERMISSION_GRANTED) {
            if (permissionRepresent.status != GRANTED) {
                permissions.forEach {
                    it.status = GRANTED
                    it.save()
                }
            }
        } else {
            when (permissionRepresent.status) {
                GRANTED -> {
                    permissions.forEach {
                        it.status = DENIED
                        it.save()
                    }
                }
                DENIED -> {
                    if (shouldShowRequestPermissionRationale(permissionRepresent.name) != permissionRepresent.isShow) {
                        permissions.forEach {
                            it.status = NEVER
                            it.save()
                        }
                    }
                }
                else -> {
                    if (shouldShowRequestPermissionRationale(permissionRepresent.name) != permissionRepresent.isShow) {
                        permissions.forEach {
                            it.status = DENIED
                            it.save()
                        }
                    }
                }
            }
        }
    }

    fun changeStatusPermissionOld(
        permissions: List<Permission>,
        resultCode: Int
    ) {
        val permissionRepresent = permissions[0]
        var result = if (resultCode == 1) checkSelfPermission(permissionRepresent.name) else resultCode

        if (result == PERMISSION_GRANTED) {
            if (permissionRepresent.status != GRANTED) {
                permissions.forEach {
                    it.status = GRANTED
                    it.save()
                }
            }
        } else {
            when (permissionRepresent.status) {
                GRANTED -> {
                    permissions.forEach {
                        it.status = DENIED
                        it.save()
                    }
                }
                DENIED -> {
                    if (!shouldShowRequestPermissionRationale(permissionRepresent.name)) {
                        permissions.forEach {
                            it.status = NEVER
                            it.save()
                        }
                    }
                }
                else -> {
                    if (shouldShowRequestPermissionRationale(permissionRepresent.name)) {
                        permissions.forEach {
                            it.status = DENIED
                            it.save()
                        }
                    }
                }
            }
        }
    }
    */
}