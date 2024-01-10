package com.utesocial.android.core.presentation.main.element.partial

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.google.android.material.search.SearchView
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.main.adapter.SearchLoadStateAdapter
import com.utesocial.android.core.presentation.main.adapter.SearchPagedAdapter
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.databinding.ActivityMainSearchBinding

class MainActivitySearch(
    private val activity: MainActivity,
    private val binding: ActivityMainSearchBinding
) : MainPartial(activity, binding) {

    fun setupRecyclerView(pagedAdapter: SearchPagedAdapter) {
        val searchLoadStateAdapter = SearchLoadStateAdapter { pagedAdapter.retry() }
        binding.recyclerViewSearch.adapter = pagedAdapter.withLoadStateFooter(searchLoadStateAdapter)

        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded = it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if(appendFailed) {
                    (loadState.append as LoadState.Error).error.let {ex ->
                        (ex as ResponseException).error?.let { error ->
//                            showError(error)
                        }
                    }
                }
                binding.shimmerFrameLayout.isVisible = loadingState
                binding.recyclerViewSearch.isVisible = !loadingState && !firstFailed
                binding.lottieEmptyView.isVisible = firstEmptyLoaded
                binding.textViewError.isVisible = firstFailed
                if(loadState.refresh is LoadState.Error) {
                    val errorState = loadState.refresh as LoadState.Error
                    val errorResponse = errorState.error as ResponseException
                    errorResponse.error?.let { error ->
                        if(error.undefinedMessage.isNullOrEmpty()) {
                            binding.textViewError.text = binding.root.context.getString(error.errorType.stringResId)
                        } else {
                            binding.textViewError.text = error.undefinedMessage
                        }
                    }
                }
            }
        }

//        pagedAdapter.addLoadStateListener { loadState ->
//            val loadingState = loadState.source.refresh is LoadState.Loading
//            val firstFailed = loadState.source.refresh is LoadState.Error
//
//            if (loadState.refresh is LoadState.Error) {
//                val errorState = loadState.refresh as LoadState.Error
//                val errorMessage = errorState.error.localizedMessage
//                if (errorMessage != null) {
//                    activity.showSnackbar(message = errorMessage)
//                }
//            }
//
//            Debug.log(
//                "MainActivitySearch",
//                "recyclerViewSearch - visible: ${binding.recyclerViewSearch.isVisible}"
//            )
//            Debug.log(
//                "MainActivitySearch",
//                "loadingState: $loadingState"
//            )
//            Debug.log(
//                "MainActivitySearch",
//                "firstFailed: $firstFailed"
//            )
//
//            if (!binding.searchView.editText.text.isNullOrEmpty()) {
//                binding.recyclerViewSearch.isVisible = !loadingState && !firstFailed
//                binding.shimmerFrameLayout.isVisible = loadingState
//                binding.lottieEmptyView.isVisible = firstFailed || (loadState.source.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0)
//            }
//        }
    }

    fun searchView(): SearchView = binding.searchView

    fun isClear() {
        binding.linearLayoutEmpty.visibility = VISIBLE

        binding.shimmerFrameLayout.visibility = GONE
        binding.recyclerViewSearch.visibility = GONE
        binding.lottieEmptyView.visibility = GONE
        binding.textViewError.visibility = GONE
    }

    fun isTyping() {
        if (binding.linearLayoutEmpty.isVisible) {
            binding.linearLayoutEmpty.visibility = GONE
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