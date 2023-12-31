package com.utesocial.android.feature_community.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentCommunityBinding
import com.utesocial.android.feature_community.presentation.adapter.CommunityAdapter
import com.utesocial.android.feature_community.presentation.adapter.CommunityFragmentsAdapter
import com.utesocial.android.feature_community.presentation.element.partial.CommunityTabItem
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.listener.PostListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {

    override lateinit var binding: FragmentCommunityBinding
    override val viewModel: CommunityViewModel by viewModels()

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
        setupTabItem()
//        setupRecyclerView()
//        setupListener()
//        observer()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
    }

    private fun setupTabItem() {
        val tabFriends = CommunityTabItem(binding, resources.getString(R.string.str_fra_community_tab_title_friends))
        val tabRequests = CommunityTabItem(binding, resources.getString(R.string.str_fra_community_tab_title_request))
        val fragmentsAdapter = CommunityFragmentsAdapter(childFragmentManager, viewLifecycleOwner)
        binding.communityViewPager.adapter = fragmentsAdapter
        TabLayoutMediator(binding.communityTabLayout, binding.communityViewPager) { tab, position ->
            val tabItem : CommunityTabItem = if(position == 0) {
                tabFriends
            } else {
                tabRequests
            }
            tab.customView = tabItem.rootView()
        }.attach()
    }

//    private fun setupListener() = binding.swipeRefreshLayout.setOnRefreshListener {
//        binding.swipeRefreshLayout.isRefreshing = false
//        viewModel.getCommunityInfo()
//    }

//    private fun observer() = lifecycleScope.launch {
//        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//            viewModel.communityState.collect {
//                if (!it.isLoading) {
//                    groups.clear()
//                    posts.clear()
//
//                    if (it.error.isNotEmpty()) {
//                        getBaseActivity().showSnackbar(message = it.error)
//                    } else {
//                        if (it.groups.isNotEmpty()) {
//                            groups.addAll(it.groups)
//                        }
//
//                        if (it.posts.isNotEmpty()) {
//                            posts.addAll(it.posts)
//                        }
//
//                        communityAdapter.notifyItemRangeChanged(0, posts.size + 1)
//                    }
//                }
//            }
//        }
//    }
}