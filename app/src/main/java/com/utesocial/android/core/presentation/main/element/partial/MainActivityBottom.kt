package com.utesocial.android.core.presentation.main.element.partial

import androidx.core.view.forEach
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.OnScrollStateChangedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.utesocial.android.R
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.core.presentation.view.BottomAppBarCustom
import com.utesocial.android.databinding.ActivityMainBottomBinding

class MainActivityBottom(
    activity: MainActivity,
    private val binding: ActivityMainBottomBinding
) : MainPartial(activity, binding) {

    fun setup() = binding.bottomNavigationView.menu.forEach {
        when (it.itemId) {
            R.id.item_fra_home -> it.tooltipText = "Trang chủ"
            R.id.item_fra_friends_list -> it.tooltipText = "Bạn bè"
            R.id.item_fra_friend_request -> it.tooltipText = "Yêu cầu"
            R.id.item_fra_settings -> it.tooltipText = "Cài đặt"
        }
    }

    fun bottomAppBar(): BottomAppBarCustom = binding.bottomAppBar

    fun bottomNavigationView(): BottomNavigationView = binding.bottomNavigationView

    fun bottomViewOnScrollBehavior(): OnScrollStateChangedListener = bottomAppBar().getDefaultScrollBehavior()
}