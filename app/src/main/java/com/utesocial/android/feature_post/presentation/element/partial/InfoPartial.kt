package com.utesocial.android.feature_post.presentation.element.partial

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import com.utesocial.android.R

@SuppressLint("RestrictedApi")
abstract class InfoPartial(buttonMenu: MaterialButton) {

    init {
        val popupMenu = PopupMenu(buttonMenu.context, buttonMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_ite_post_btn, popupMenu.menu)

        if (popupMenu.menu is MenuBuilder) {
            val menuBuilder = popupMenu.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)

            for (item in menuBuilder.visibleItems) {
                val iconInsetLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10F, buttonMenu.resources.displayMetrics).toInt()
                val iconInsetRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6F, buttonMenu.resources.displayMetrics).toInt()
                if (item.icon != null) {
                    item.icon = InsetDrawable(item.icon, iconInsetLeft, 0, iconInsetRight,0)
                }
            }
        }

        buttonMenu.setOnClickListener { popupMenu.show() }
    }
}