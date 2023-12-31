package com.utesocial.android.feature_community.presentation.friendList.element

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.databinding.FragmentFriendListBinding
import com.utesocial.android.feature_community.presentation.adapter.FriendsListPagedAdapter
import com.utesocial.android.feature_community.presentation.adapter.FriendsLoadStateAdapter
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FriendsListFragment : BaseFragment<FragmentFriendListBinding>() {

    override lateinit var binding: FragmentFriendListBinding
    override val viewModel : CommunityViewModel by viewModels()

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFriendListBinding {
        binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding
    }

    private val pagedAdapter : FriendsListPagedAdapter by lazy {
        FriendsListPagedAdapter(viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
            friendsSearchBar.isSubmitButtonEnabled = false
            viewModel.apply {
                disposable.add(
                    searchFriendsList
                        .distinctUntilChanged()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribe {
                            Debug.log("SearchLiveData", it.toString())
                            Handler(Looper.getMainLooper()).post {
                                refreshData()
                            }
                        }
                )

                disposable.add(
                    friendsSearchBar
                        .queryTextChanges()
                        .skipInitialValue()
                        .subscribe { charSequence ->
                            if(charSequence.toString().isNotEmpty() && charSequence.toString().trim().isEmpty()) {
                                Handler(Looper.getMainLooper()).post {
                                    friendsSearchBar.setQuery("", false)
                                }
                            } else {
                                searchFriendsList.onNext(charSequence.toString())
                            }
                        }
                )
            }
            friendsSearchBar.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
                friendsSearchBar.apply {
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
        val friendsLoadStateAdapter = FriendsLoadStateAdapter() {
            pagedAdapter.retry()
        }

        binding.rcvFriendsList.adapter = pagedAdapter.withLoadStateFooter(friendsLoadStateAdapter)
        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded = it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if(appendFailed) {
                    (loadState.append as LoadState.Error).error.let {ex ->
                        (ex as ResponseException).error?.let {error ->
                            showError(error)
                        }
                    }
                }
                binding.shimmerLayout.isVisible = loadingState
                binding.rcvFriendsList.isVisible = !loadingState && !firstFailed
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
        viewModel.getFriendsList()
            .observe(viewLifecycleOwner) { paging ->
                pagedAdapter.submitData(viewLifecycleOwner.lifecycle, paging)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

}