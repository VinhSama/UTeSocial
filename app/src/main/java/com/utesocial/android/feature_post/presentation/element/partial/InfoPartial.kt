package com.utesocial.android.feature_post.presentation.element.partial

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import com.utesocial.android.R
import com.utesocial.android.feature_post.presentation.listener.PostListener

@SuppressLint("RestrictedApi")
abstract class InfoPartial(private val buttonMenu: MaterialButton) {

    private val popupMenu: PopupMenu = PopupMenu(buttonMenu.context, buttonMenu)

    init {
        popupMenu.menuInflater.inflate(R.menu.menu_ite_post_btn, popupMenu.menu)
        if (popupMenu.menu is MenuBuilder) {
            val menuBuilder = popupMenu.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)

            for (item in menuBuilder.visibleItems) {
                val iconInsetLeft = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10F,
                    buttonMenu.resources.displayMetrics
                ).toInt()
                val iconInsetRight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    6F,
                    buttonMenu.resources.displayMetrics
                ).toInt()
                if (item.icon != null) {
                    item.icon = InsetDrawable(item.icon, iconInsetLeft, 0, iconInsetRight, 0)
                }
            }
        }
    }

    fun hideButtonMenu() {
        buttonMenu.visibility = View.GONE
    }

    fun setupListener(
        listener: PostListener,
        postId: String
    ) {
        buttonMenu.visibility = View.VISIBLE

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_remove -> {
                    listener.onDeletePost(postId)
                    true
                }

                else -> false
            }
        }

        buttonMenu.setOnClickListener { popupMenu.show() }
    }
}