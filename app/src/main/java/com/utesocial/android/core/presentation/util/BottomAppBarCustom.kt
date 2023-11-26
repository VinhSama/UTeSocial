package com.utesocial.android.core.presentation.util

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.core.animation.doOnCancel
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.OnScrollStateChangedListener
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.STATE_SCROLLED_UP
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.utesocial.android.R

class BottomAppBarCustom : BottomAppBar {

    private val fabIdDefault = -1
    private val sizeCornerDefault by lazy { resources.getDimension(R.dimen.bab_custom_corner_default) }
    private val sizeCornerMax by lazy { resources.getDimension(R.dimen.bab_custom_corner_max) }

    @IdRes private val fabId: Int

    private val sizeCornerAll: Float
    private val sizeCornerBottomLeft: Float
    private val sizeCornerBottomRight: Float
    private val sizeCornerTopLeft: Float
    private val sizeCornerTopRight: Float

    private var oaScrollUp: ObjectAnimator?
    private var oaScrollDown: ObjectAnimator?
    private val onScrollBehavior: OnScrollStateChangedListener

    constructor(context: Context) : this(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, com.google.android.material.R.attr.bottomAppBarStyle)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        val attrsCustom: IntArray = intArrayOf(
            R.attr.fab,
            R.attr.sizeCornerAll,
            R.attr.sizeCornerBottomLeft,
            R.attr.sizeCornerBottomRight,
            R.attr.sizeCornerTopLeft,
            R.attr.sizeCornerTopRight
        )

        context.theme.obtainStyledAttributes(attrs, attrsCustom, defStyleAttr, 0).apply {
            fabId = getResourceId(R.styleable.BottomAppBarCustom_fab, fabIdDefault)

            sizeCornerAll = validSizeCorner(getDimension(
                R.styleable.BottomAppBarCustom_sizeCornerAll,
                sizeCornerDefault
            ))
            sizeCornerBottomLeft = validSizeCorner(getDimension(
                R.styleable.BottomAppBarCustom_sizeCornerBottomLeft,
                sizeCornerDefault
            ))
            sizeCornerBottomRight = validSizeCorner(getDimension(
                R.styleable.BottomAppBarCustom_sizeCornerBottomRight,
                sizeCornerDefault
            ))
            sizeCornerTopLeft = validSizeCorner(getDimension(
                R.styleable.BottomAppBarCustom_sizeCornerTopLeft,
                sizeCornerDefault
            ))
            sizeCornerTopRight = validSizeCorner(getDimension(
                R.styleable.BottomAppBarCustom_sizeCornerTopRight,
                sizeCornerDefault
            ))
        }

        oaScrollUp = null
        oaScrollDown = null

        onScrollBehavior = OnScrollStateChangedListener { _, newState ->
            cancelAnimatorScroll()
            if (newState == STATE_SCROLLED_UP) {
                oaScrollUp?.start()
            } else {
                oaScrollDown?.start()
            }
        }
    }

    private fun cancelAnimatorScroll() {
        if (oaScrollUp?.isRunning == true) {
            oaScrollUp?.cancel()
        }

        if (oaScrollDown?.isRunning == true) {
            oaScrollDown?.cancel()
        }
    }

    private fun validSizeCorner(size: Float): Float {
        return when (size) {
            in sizeCornerDefault..sizeCornerMax -> size
            else -> if (size < sizeCornerDefault) {
                sizeCornerDefault
            } else {
                sizeCornerMax
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setFAB()
        setCorner()
    }

    private fun setFAB() {
        if (fabId != fabIdDefault) {
            val view: View = (this@BottomAppBarCustom.parent as View).findViewById(fabId)

            if (view::class == FloatingActionButton::class) {
                val floatingActionButton = view as FloatingActionButton

                val positionDown = resources.getDimension(R.dimen.transition_y_bnv_height)
                val positionUp = resources.getDimension(R.dimen.transition_y_fab_home_margin)
                val durationDown = resources.getInteger(R.integer.duration_200).toLong()
                val durationUp = resources.getInteger(R.integer.duration_85).toLong()

                oaScrollUp = ObjectAnimator.ofFloat(floatingActionButton, "translationY", positionDown, positionUp).apply {
                    duration = durationUp
                    doOnCancel { floatingActionButton.translationY = positionUp }
                }
                oaScrollDown = ObjectAnimator.ofFloat(floatingActionButton, "translationY", positionUp, positionDown).apply {
                    duration = durationDown
                    doOnCancel { floatingActionButton.translationY = positionDown }
                }

                addOnScrollStateChangedListener(onScrollBehavior)
            }
        }
    }

    private fun setCorner() {
        val backgroundShapeDrawable = background as MaterialShapeDrawable
        val shapeAppearanceModelBuilder = backgroundShapeDrawable.shapeAppearanceModel.toBuilder()

        if (sizeCornerAll != sizeCornerDefault) {
            shapeAppearanceModelBuilder.setAllCorners(CornerFamily.ROUNDED, sizeCornerAll)
        } else {
            shapeAppearanceModelBuilder.setBottomLeftCorner(CornerFamily.ROUNDED, sizeCornerBottomLeft)
                .setBottomRightCorner(CornerFamily.ROUNDED, sizeCornerBottomRight)
                .setTopLeftCorner(CornerFamily.ROUNDED, sizeCornerTopLeft)
                .setTopRightCorner(CornerFamily.ROUNDED, sizeCornerTopRight)
        }

        backgroundShapeDrawable.shapeAppearanceModel = shapeAppearanceModelBuilder.build()
    }

    fun getDefaultScrollBehavior(): OnScrollStateChangedListener = onScrollBehavior
}