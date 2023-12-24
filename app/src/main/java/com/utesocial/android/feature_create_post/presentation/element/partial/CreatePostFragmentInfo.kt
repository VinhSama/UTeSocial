package com.utesocial.android.feature_create_post.presentation.element.partial

import com.utesocial.android.core.presentation.base.BaseActivity
import com.utesocial.android.databinding.FragmentCreatePostInfoBinding

class CreatePostFragmentInfo(private val binding: FragmentCreatePostInfoBinding) {

    fun setupListener(activity: BaseActivity<*>) = binding.buttonBack.setOnClickListener {
        activity.onBackPressedDispatcher.onBackPressed()
    }

}