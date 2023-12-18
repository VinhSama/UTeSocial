package com.utesocial.android.feature_post.presentation.element

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.feature_post.presentation.adapter.PostImageAdapter
import com.utesocial.android.databinding.FragmentPostBinding
import com.utesocial.android.feature_post.presentation.element.partial.InfoPost

class PostFragment : BaseFragment<FragmentPostBinding>() {

    override lateinit var binding: FragmentPostBinding
    override val viewModel = null
    private val args: PostFragmentArgs by navArgs()

    private val post: Post by lazy { args.post }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPostBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false)
        return binding
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupRecyclerView()
        setupListener()
    }

    private fun setupBinding() {
        binding.post = post
        InfoPost(binding.info)
    }

    private fun setupRecyclerView() {
        val postImageAdapter = PostImageAdapter(this@PostFragment, post.images)
        binding.recyclerViewImage.adapter = postImageAdapter
    }

    private fun setupListener() {
        binding.textViewContent.setOnClickListener {
            val lines = if (binding.textViewContent.lineCount == 3) 1000 else 3
            ObjectAnimator.ofInt(binding.textViewContent, "maxLines", lines).apply {
                duration = resources.getInteger(R.integer.duration_200).toLong()
                start()
            }
        }
    }
}