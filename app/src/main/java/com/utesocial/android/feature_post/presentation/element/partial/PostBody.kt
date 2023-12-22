package com.utesocial.android.feature_post.presentation.element.partial

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.CountDownTimer
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.presentation.adapter.PostBodyImageAdapter
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener
import com.utesocial.android.databinding.ItemPostBodyBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostResource
import com.utesocial.android.feature_post.presentation.adapter.PostModelBodyImageAdapter

class PostBody(val binding: ItemPostBodyBinding) {

    init {
        val resource = binding.root.resources
        setAnimatorContent(resource)
        setAnimatorQuantity(resource)
    }

    private fun setAnimatorContent(resources: Resources) = binding.textViewContent.setOnClickListener {
        val lines = if (binding.textViewContent.lineCount == 3) 1000 else 3
        ObjectAnimator.ofInt(binding.textViewContent, "maxLines", lines).apply {
            duration = resources.getInteger(R.integer.duration_200).toLong()
            start()
        }
    }

    private fun setAnimatorQuantity(resources: Resources) {
        val duration = resources.getInteger(R.integer.duration_200).toLong()

        val oaShowQuantity = ObjectAnimator.ofFloat(binding.linearLayoutQuantity, "alpha", 0F, 1F).setDuration(duration)
        val oaHideQuantity = ObjectAnimator.ofFloat(binding.linearLayoutQuantity, "alpha", 1F, 0F).setDuration(duration)
        val countDownTimer = object : CountDownTimer(binding.root.resources.getInteger(R.integer.duration_3000).toLong(), 1000) {

            override fun onTick(p0: Long) {}

            override fun onFinish() = oaHideQuantity.start()
        }

        binding.viewPagerImage.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                val currentPosition = position + 1
                val maxPosition = binding.viewPagerImage.adapter?.itemCount

                binding.textViewQuantity.text = resources.getString(R.string.str_ite_post_body_tv_quantity, currentPosition, maxPosition)
                if (binding.linearLayoutQuantity.alpha == 0F) {
                    oaShowQuantity.start()
                }

                countDownTimer.cancel()
                countDownTimer.start()
            }
        })
    }

    fun setupImages(
        lifecycleOwner: LifecycleOwner,
        post: Post,
        listener: PostBodyImageListener
    ) {
        binding.textViewQuantity.text = "0/0"

        val postBodyImageAdapter = PostBodyImageAdapter(lifecycleOwner, post, listener)
        binding.viewPagerImage.adapter = postBodyImageAdapter
    }

    fun setupImages(
        lifecycleOwner: LifecycleOwner,
        postModel: PostModel,
        listener: PostModelBodyImageAdapter.PostBodyImageListener
    ) {
        binding.textViewQuantity.text = "0/0"

        val postBodyImageAdapter = PostModelBodyImageAdapter(lifecycleOwner, postModel, listener)
        binding.viewPagerImage.adapter = postBodyImageAdapter
    }
}