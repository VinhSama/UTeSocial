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
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.showError
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
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        refreshData()
    }

    private val pagedAdapter : PostPagedAdapter by lazy {
        PostPagedAdapter(viewLifecycleOwner, object : PostModelBodyImageAdapter.PostBodyImageListener {
            override fun onClick(postModel: PostModel) {
                val action = HomeFragmentDirections.actionHomePost(postModel)
                getBaseActivity().navController()?.navigate(action)
                getBaseActivity().handleBar(false)
            }
        }, viewModel.getCurrentUserId(), object : PostPagedAdapter.OnItemActionsListener {
            override fun onLikeChanged(isChecked: Boolean, postModel: PostModel) {
                if(isChecked) {
                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setupRecyclerView() {

        val postLoadStateAdapter = PostLoadStateAdapter() {
            pagedAdapter.retry()
        }
        binding.recyclerViewPost.adapter = pagedAdapter.withLoadStateFooter(postLoadStateAdapter)
        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded = it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if(appendFailed) {
                    (loadState.append as LoadState.Error).error.let {ex ->
                        (ex as ResponseException).error?.let { error ->
                            showError(error)
                        }
                    }
                }
                binding.shimmerFrameLayout.isVisible = loadingState
                binding.recyclerViewPost.isVisible = !loadingState && !firstFailed
                binding.loadStateViewGroup.isVisible = firstEmptyLoaded || firstFailed
                binding.lottieEmptyView.isVisible = firstEmptyLoaded
                binding.txvErrorMessage.isVisible = firstFailed

                if(loadState.refresh is LoadState.Error) {
                    val errorState = loadState.refresh as LoadState.Error
                    val errorResponse = errorState.error as ResponseException
                    errorResponse.error?.let { error ->
                        if(error.undefinedMessage.isNullOrEmpty()) {
                            binding.txvErrorMessage.text = getString(error.errorType.stringResId)
                        } else {
                            binding.txvErrorMessage.text = error.undefinedMessage
                        }
                    }
                }
            }
        }
    }


    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getFeedPosts()
            .observe(viewLifecycleOwner) { pagingData ->
                pagedAdapter.submitData(lifecycle, pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

}