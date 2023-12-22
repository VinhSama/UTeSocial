package com.utesocial.android.feature_community.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentCommunityBinding
import com.utesocial.android.feature_community.presentation.adapter.CommunityAdapter
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener
import kotlinx.coroutines.launch

class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {

    override lateinit var binding: FragmentCommunityBinding
    override val viewModel: CommunityViewModel by viewModels { CommunityViewModel.Factory }

    private lateinit var communityAdapter: CommunityAdapter
    private val groups: ArrayList<Group> by lazy { ArrayList() }
    private val posts: ArrayList<Post> by lazy { ArrayList() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)
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
        observer()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        communityAdapter = CommunityAdapter(this@CommunityFragment, groups, posts, object : PostBodyImageListener {

            override fun onClick(post: Post) {
//                val action = CommunityFragmentDirections.actionCommunityPost(post)
//                getBaseActivity().navController()?.navigate(action)
//                getBaseActivity().handleBar(false)
            }
        })
        binding.recyclerViewCommunity.adapter = communityAdapter
    }

    private fun setupListener() = binding.swipeRefreshLayout.setOnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getCommunityInfo()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.communityState.collect {
                if (!it.isLoading) {
                    groups.clear()
                    posts.clear()

                    if (it.error.isNotEmpty()) {
                        getBaseActivity().showSnackbar(message = it.error)
                    } else {
                        if (it.groups.isNotEmpty()) {
                            groups.addAll(it.groups)
                        }

                        if (it.posts.isNotEmpty()) {
                            posts.addAll(it.posts)
                        }

                        communityAdapter.notifyItemRangeChanged(0, posts.size + 1)
                    }
                }
            }
        }
    }
}