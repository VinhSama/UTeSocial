package com.utesocial.android.feature_community.presentation.friendList.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.databinding.FragmentFriendListBinding
import com.utesocial.android.feature_community.presentation.adapter.FriendsListPagedAdapter
import com.utesocial.android.feature_community.presentation.adapter.FriendsLoadStateAdapter
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsListFragment : BaseFragment<FragmentFriendListBinding>() {

    override lateinit var binding: FragmentFriendListBinding
    override val viewModel : CommunityViewModel by viewModels()

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFriendListBinding {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, false)
        binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding
    }

    private val pagedAdapter : FriendsListPagedAdapter by lazy {
        FriendsListPagedAdapter(viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.setOnRefreshListener {
//            refreshData()
        }
        viewLifecycleOwner.lifecycleScope.launch {
//            refreshData()
        }
    }

    private fun setupRecyclerView() {
        val friendsLoadStateAdapter = FriendsLoadStateAdapter() {
            pagedAdapter.retry()
        }
        binding.rcvFriendsList.adapter = pagedAdapter.withLoadStateFooter(friendsLoadStateAdapter)
        pagedAdapter.addLoadStateListener { loadState ->
            val loadingState = loadState.source.refresh is LoadState.Loading
            val firstFailed = loadState.source.refresh is LoadState.Error
            if(loadState.refresh is LoadState.Error) {
                val errorState = loadState.refresh as LoadState.Error
                val errorResponse = errorState.error as ResponseException
                errorResponse.getError()?.let { error ->
                    if(error.undefinedMessage.isNullOrEmpty()) {
                        binding.txvErrorMessage.text = getString(error.errorType.stringResId)
                    } else {
                        binding.txvErrorMessage.text = error.undefinedMessage
                    }
                    binding.txvErrorMessage.isVisible = true
                    binding.rcvFriendsList.isVisible = false
                    binding.shimmerLayout.isVisible = false
                    binding.lottieEmptyView.isVisible = false
                }
                val errorMessage = errorState.error.localizedMessage
                if (errorMessage != null) {
                    getBaseActivity().showSnackbar(message = errorMessage)
                }
            }
            binding.rcvFriendsList.isVisible = !loadingState && !firstFailed
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getFriendsList()
            .observe(viewLifecycleOwner) { paging ->
                pagedAdapter.submitData(viewLifecycleOwner.lifecycle, paging)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

}