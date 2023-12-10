package com.utesocial.android.core.presentation.post.element.partial

import android.animation.ObjectAnimator
import androidx.lifecycle.LifecycleOwner
import com.utesocial.android.R
import com.utesocial.android.core.presentation.post.adapter.PostBodyImageAdapter
import com.utesocial.android.databinding.ItemPostBodyBinding

class PostBody(val binding: ItemPostBodyBinding) {

    init {
        val textViewContext = binding.textViewContent
        val durationAnimator = binding.root.resources.getInteger(R.integer.duration_200).toLong()

        binding.textViewContent.setOnClickListener {
            if (binding.textViewContent.lineCount == 3) {
                ObjectAnimator.ofInt(textViewContext, "maxLines", 1000).apply {
                    duration = durationAnimator
                    start()
                }
            } else {
                ObjectAnimator.ofInt(textViewContext, "maxLines", 3).apply {
                    duration = durationAnimator
                    start()
                }
            }
        }
    }

    fun setupImages(lifecycleOwner: LifecycleOwner, images: List<String>) {
        val postBodyImageAdapter = PostBodyImageAdapter(lifecycleOwner, images)
        binding.viewPagerImage.adapter = postBodyImageAdapter
    }
}