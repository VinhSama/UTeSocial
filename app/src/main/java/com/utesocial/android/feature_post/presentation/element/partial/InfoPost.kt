package com.utesocial.android.feature_post.presentation.element.partial

import com.utesocial.android.core.presentation.base.BaseActivity
import com.utesocial.android.databinding.FragmentPostInfoBinding

class InfoPost(private val binding: FragmentPostInfoBinding) :
    InfoPartial(binding.imageViewPrivacy, binding.buttonMenu) {

    fun setupListener(activity: BaseActivity<*>) = binding.buttonBack.setOnClickListener {
        activity.onBackPressedDispatcher.onBackPressed()
    }
}