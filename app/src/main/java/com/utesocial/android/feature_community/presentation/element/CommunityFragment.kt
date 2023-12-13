package com.utesocial.android.feature_community.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.carousel.CarouselLayoutManager
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentCommunityBinding
import com.utesocial.android.feature_community.presentation.adapter.CommunityAdapter
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.presentation.adapter.PostAdapter
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener

class CommunityFragment : BaseFragment() {

    private lateinit var binding: FragmentCommunityBinding
    private val viewModel: CommunityViewModel by viewModels { CommunityViewModel.Factory }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)
        return binding
    }

    override fun initViewModel(): ViewModel = viewModel

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@CommunityFragment }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val communityAdapter = CommunityAdapter(this@CommunityFragment, viewModel.communityState.value.joinGroups)
        binding.recyclerViewCommunity.adapter = communityAdapter

        val postAdapter = PostAdapter(this@CommunityFragment, viewModel.communityState.value.postsGroup, object : PostBodyImageListener {

            override fun onClick(post: Post) {

            }
        })
        binding.recyclerViewPost.adapter = postAdapter
    }
}