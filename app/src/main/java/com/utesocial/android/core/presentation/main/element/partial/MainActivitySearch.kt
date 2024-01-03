package com.utesocial.android.core.presentation.main.element.partial

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.google.android.material.search.SearchView
import com.utesocial.android.core.presentation.main.adapter.SearchUsersLoadStateAdapter
import com.utesocial.android.core.presentation.main.adapter.SearchUsersPagedAdapter
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.databinding.ActivityMainSearchBinding

class MainActivitySearch(
    private val activity: MainActivity,
    private val binding: ActivityMainSearchBinding
) : MainPartial(activity, binding) {

    fun setupRecyclerView(pagedAdapter: SearchUsersPagedAdapter) {
        val searchUsersLoadStateAdapter = SearchUsersLoadStateAdapter { pagedAdapter.retry() }
        binding.recyclerViewSearch.adapter = pagedAdapter.withLoadStateFooter(searchUsersLoadStateAdapter)

        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded = it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if (appendFailed) {
                    (loadState.append as LoadState.Error).error.let { ex ->
                        (ex as ResponseException).error?.let { error ->
                            activity.showError(error)
                        }
                    }
                }

                binding.shimmerFrameLayout.isVisible = loadingState
                binding.recyclerViewSearch.isVisible = !loadingState && !firstFailed
                binding.textViewError.isVisible = firstFailed

                if (binding.textViewEmpty.isVisible) {
                    binding.lottieEmptyView.visibility = GONE
                } else {
                    binding.lottieEmptyView.isVisible = firstEmptyLoaded
                }

                if (loadState.refresh is LoadState.Error) {
                    val errorState = loadState.refresh as LoadState.Error
                    val errorResponse = errorState.error as ResponseException
                    errorResponse.error?.let { error ->
                        if(error.undefinedMessage.isNullOrEmpty()) {
                            binding.textViewError.text = activity.getString(error.errorType.stringResId)
                        } else {
                            binding.textViewError.text = error.undefinedMessage
                        }
                    }
                }
            }
        }
    }

    fun searchView(): SearchView = binding.searchView

    fun isClear() {
        binding.textViewEmpty.visibility = VISIBLE
        binding.shimmerFrameLayout.visibility = GONE
        binding.recyclerViewSearch.visibility = GONE
        binding.lottieEmptyView.visibility = GONE
        binding.textViewError.visibility = GONE
    }

    fun isTyping() {
        if (binding.textViewEmpty.isVisible) {
            binding.textViewEmpty.visibility = GONE
        }

        if (binding.recyclerViewSearch.isVisible) {
            binding.recyclerViewSearch.visibility = GONE
        }

        if (binding.lottieEmptyView.isVisible) {
            binding.lottieEmptyView.visibility = GONE
        }

        if (!binding.shimmerFrameLayout.isVisible) {
            binding.shimmerFrameLayout.visibility = VISIBLE
        }
    }
}