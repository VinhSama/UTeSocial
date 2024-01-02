package com.utesocial.android.core.presentation.util

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.navigation.ActivityNavigator
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.google.android.material.navigation.NavigationBarView
import com.utesocial.android.core.presentation.base.BaseActivity
import java.lang.ref.WeakReference

object NavigationUICustom {

    fun setupWithNavController(
        activity: BaseActivity<*>,
        navigationBarView: NavigationBarView,
        navController: NavController,
        @AnimRes animEnter: Int = androidx.navigation.ui.R.anim.nav_default_enter_anim,
        @AnimRes animExit: Int = androidx.navigation.ui.R.anim.nav_default_exit_anim,
        @AnimRes animPopEnter: Int = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
        @AnimRes animPopExit: Int = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
        @AnimatorRes animatorEnter: Int = androidx.navigation.ui.R.animator.nav_default_enter_anim,
        @AnimatorRes animatorExit: Int = androidx.navigation.ui.R.animator.nav_default_exit_anim,
        @AnimatorRes animatorPopEnter: Int = androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
        @AnimatorRes animatorPopExit: Int = androidx.navigation.ui.R.animator.nav_default_pop_exit_anim,
        @IdRes vararg itemHideActionBar: Int
    ) {
        itemHideActionBar.forEach check@ { id ->
            navigationBarView.menu.forEach {
                if (it.itemId == id) {
                    return@check
                }
            }

            throw NavigationUICustomException("ID inside 'itemHideActionBar' must be a subset of NavigationBarView.menu", NotFoundException())
        }

        navigationBarView.setOnItemSelectedListener {
            onNavDestinationSelected(
                activity, it, navController,
                animEnter, animExit, animPopEnter, animPopExit,
                animatorEnter, animatorExit, animatorPopEnter, animatorPopExit,
                itemHideActionBar = itemHideActionBar
            )
        }

        val weakReference = WeakReference(navigationBarView)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {

            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                val view = weakReference.get()

                if (view == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }

                if (destination is FloatingWindow) {
                    return
                }

                run match@ {
                    view.menu.forEach {
                        if (destination.matchDestination(it.itemId)) {
                            it.isChecked = true
                            return@match
                        }
                    }
                }
            }
        })
    }

    private fun onNavDestinationSelected(
        activity: BaseActivity<*>,
        item: MenuItem,
        navController: NavController,
        @AnimRes animEnter: Int,
        @AnimRes animExit: Int,
        @AnimRes animPopEnter: Int,
        @AnimRes animPopExit: Int,
        @AnimatorRes animatorEnter: Int,
        @AnimatorRes animatorExit: Int,
        @AnimatorRes animatorPopEnter: Int,
        @AnimatorRes animatorPopExit: Int,
        @IdRes vararg itemHideActionBar: Int
    ): Boolean {
        val navOptionsBuilder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true)

        if (navController.currentDestination!!.parent!!.findNode(item.itemId) is ActivityNavigator.Destination) {
            navOptionsBuilder.setEnterAnim(animEnter).setExitAnim(animExit).setPopEnterAnim(animPopEnter).setPopExitAnim(animPopExit)
        } else {
            navOptionsBuilder.setEnterAnim(animatorEnter).setExitAnim(animatorExit).setPopEnterAnim(animatorPopEnter).setPopExitAnim(animatorPopExit)
        }

        if ((item.order and Menu.CATEGORY_SECONDARY) == 0) {
            navOptionsBuilder.setPopUpTo(navController.graph.findStartDestination().id, inclusive = false, saveState = true)
        }

        val navOptions = navOptionsBuilder.build()
        return try {
            navController.navigate(item.itemId, null, navOptions)
            /*if (!itemHideActionBar.contains(item.itemId)) {
                activity.handleActionBar(true)
            } else {
                activity.handleActionBar(false)
            }*/

            navController.currentDestination?.matchDestination(item.itemId) == true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun NavDestination.matchDestination(@IdRes destinationId: Int): Boolean = hierarchy.any { it.id == destinationId }
}