package com.utesocial.android.feature_home.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.feature_post.presentation.adapter.PostAdapter
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener
import com.utesocial.android.databinding.FragmentHomeBinding
import com.utesocial.android.feature_home.presentation.state_holder.HomeViewModel
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostResource
import com.utesocial.android.feature_post.presentation.adapter.PostLoadStateAdapter
import com.utesocial.android.feature_post.presentation.adapter.PostModelBodyImageAdapter
import com.utesocial.android.feature_post.presentation.adapter.PostPagedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override lateinit var binding: FragmentHomeBinding
    override val viewModel: HomeViewModel by viewModels()

    private val data: ArrayList<Post> by lazy { ArrayList() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        refreshData()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
    }

    private val pagedAdapter : PostPagedAdapter by lazy {
        PostPagedAdapter(viewLifecycleOwner, object : PostModelBodyImageAdapter.PostBodyImageListener {
            override fun onClick(postModel: PostModel) {
                val action = HomeFragmentDirections.actionHomePost(postModel)
                getBaseActivity().navController()?.navigate(action)
                getBaseActivity().handleBar(false)
            }
        })
    }

    private fun setupRecyclerView() {
//        val pagedAdapter = PostPagedAdapter(viewLifecycleOwner, object : PostModelBodyImageAdapter.PostBodyImageListener {
//            override fun onClick(postResource: PostResource) {
//                Toast.makeText(requireActivity(), "OnClick", Toast.LENGTH_SHORT).show()
//            }
//
//        })
        binding.recyclerViewPost.setHasFixedSize(true)
        val postLoadStateAdapter = PostLoadStateAdapter() {
            pagedAdapter.retry()
        }
        binding.recyclerViewPost.adapter = pagedAdapter.withLoadStateFooter(postLoadStateAdapter)
        pagedAdapter.addLoadStateListener { loadState ->
            val firstInitOrFirstFailed = loadState.source.refresh is LoadState.Loading || loadState.source.refresh is LoadState.Error
            if(loadState.refresh is LoadState.Error) {
                val errorState = loadState.refresh as LoadState.Error
                val errorMessage = errorState.error.localizedMessage
                if (errorMessage != null) {
                    getBaseActivity().showSnackbar(message = errorMessage)
                }
            }
            binding.recyclerViewPost.isVisible = !firstInitOrFirstFailed
            binding.shimmerFrameLayout.isVisible = firstInitOrFirstFailed
            binding.textViewEmpty.isVisible = loadState.source.refresh is LoadState.Error || (loadState.source.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0)
        }
    }


    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getFeedPosts(10)
            .observe(viewLifecycleOwner) { pagingData ->
                pagedAdapter.submitData(lifecycle, pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeState.collect {
                if (!it.isLoading) {
                    data.clear()

                    if (it.posts.isNotEmpty()) {
                        data.addAll(it.posts)
                    } else if (it.error.isNotEmpty()) {
                        getBaseActivity().showSnackbar(message = it.error)
                    }
                }
            }
        }
    }
}