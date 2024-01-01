package com.utesocial.android.feature_community.presentation.friendRequest.element

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.databinding.FragmentFriendRequestBinding
import com.utesocial.android.feature_community.domain.model.FriendRequest
import com.utesocial.android.feature_community.presentation.adapter.FriendRequestsLoadStateAdapter
import com.utesocial.android.feature_community.presentation.adapter.FriendRequestsPagedAdapter
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FriendRequestFragment : BaseFragment<FragmentFriendRequestBinding>() {
    override lateinit var binding: FragmentFriendRequestBinding
    override val viewModel: CommunityViewModel by viewModels()
    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFriendRequestBinding {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        return binding
    }

    private val pagedAdapter : FriendRequestsPagedAdapter by lazy {
        FriendRequestsPagedAdapter(object : FriendRequestsPagedAdapter.RequestItemOnActions {
            override fun onAcceptClick(request: FriendRequest) {
                viewModel.onAcceptRequest(request)
                    .observe(viewLifecycleOwner) { response ->
                        if(response.isFailure()) {
                            showError(response)
                        }
                    }
            }

            override fun onDenyClick(request: FriendRequest) {
                viewModel.onDenyRequest(request)
                    .observe(viewLifecycleOwner) { response ->
                        if(response.isFailure()) {
                            showError(response)
                        }
                    }
            }

            override fun onProfileClick(request: FriendRequest) {
                viewModel.onProfileClick(request)
            }

        })
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
            requestsSearchBar.isSubmitButtonEnabled = false
            viewModel.apply {
                disposable.add(
                    searchFriendRequest
                        .distinctUntilChanged()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribe {
                            Handler(Looper.getMainLooper()).post {
                                refreshData()
                            }
                        }
                )
                disposable.add(
                    requestsSearchBar
                        .queryTextChanges()
                        .skipInitialValue()
                        .subscribe { charSequence ->
                            if(charSequence.toString().isNotEmpty() && charSequence.toString().trim().isEmpty()) {
                                Handler(Looper.getMainLooper()).post {
                                    requestsSearchBar.setQuery("", false)
                                }
                            } else {
                                searchFriendRequest.onNext(charSequence.toString())
                            }
                        }
                )
            }

            requestsSearchBar.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
                requestsSearchBar.apply {
                    if(query.isEmpty()) {
                        isIconified = true
                    } else {
                        setQuery("", true)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val friendRequestsLoadStateAdapter = FriendRequestsLoadStateAdapter() {
            pagedAdapter.retry()
        }
        binding.rcvRequestsList.adapter = pagedAdapter.withLoadStateFooter(friendRequestsLoadStateAdapter)
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
                binding.shimmerLayout.isVisible = loadingState
                binding.rcvRequestsList.isVisible = !loadingState && !firstFailed
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
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getFriendRequests()
            .observe(viewLifecycleOwner) { paging ->
                pagedAdapter.submitData(viewLifecycleOwner.lifecycle, paging)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }
}