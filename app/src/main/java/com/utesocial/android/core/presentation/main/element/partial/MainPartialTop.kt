package com.utesocial.android.core.presentation.main.element.partial

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.search.SearchView
import com.utesocial.android.R
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.core.presentation.main.element.MainPartial
import com.utesocial.android.databinding.PartialMainTopBinding

class MainPartialTop(
    activity: MainActivity,
    private val binding: PartialMainTopBinding
) : MainPartial(activity, binding) {

    private val asBrandExit: AnimatorSet
    private val asHintEnter: AnimatorSet

    init {
        val resources = binding.root.resources
        val translationX = resources.getDimension(R.dimen.transition_x_tab_home)
        val animatorDuration = resources.getInteger(R.integer.duration_750).toLong()

        val animatorExitAlpha = AnimatorInflater.loadAnimator(binding.root.context, R.animator.animator_image_brand_exit).apply { setTarget(binding.imageViewBrand) }
        val animatorExitTranslation = ObjectAnimator.ofFloat(binding.imageViewBrand, "translationX", 0F, -translationX).apply {
            duration = animatorDuration
            interpolator = LinearInterpolator()
            startDelay = resources.getInteger(R.integer.duration_3000).toLong()
            doOnEnd { binding.imageViewBrand.visibility = GONE }
        }

        val animatorEnterAlpha = AnimatorInflater.loadAnimator(binding.root.context, R.animator.animator_text_search_enter).apply { setTarget(binding.textViewHint) }
        val animatorEnterTranslation = ObjectAnimator.ofFloat(binding.textViewHint, "translationX", translationX, 0F).apply {
            duration = animatorDuration
            interpolator = LinearInterpolator()
            startDelay = resources.getInteger(R.integer.duration_3500).toLong()
            doOnStart { binding.textViewHint.visibility = VISIBLE }
        }

        asBrandExit = AnimatorSet().apply { playTogether(animatorExitAlpha, animatorExitTranslation) }
        asHintEnter = AnimatorSet().apply { playTogether(animatorEnterAlpha, animatorEnterTranslation) }
    }

    fun setup() {
        asBrandExit.start()
        asHintEnter.start()
    }

    fun setListener(searchView: SearchView) {
        binding.imageViewBrand.setOnClickListener { searchView.show() }
        binding.textViewHint.setOnClickListener { searchView.show() }
    }

    fun appBarLayout(): AppBarLayout = binding.appBarLayout

    fun relativeLayoutAction(): RelativeLayout = binding.relativeLayoutAction
}