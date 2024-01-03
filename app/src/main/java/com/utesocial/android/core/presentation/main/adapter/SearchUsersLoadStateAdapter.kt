package com.utesocial.android.core.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.LayoutSearchUserItemLoadStateBinding

class SearchUsersLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<SearchUsersLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(val binding: LayoutSearchUserItemLoadStateBinding, retry: () -> Unit) : ViewHolder(binding.root) {

        fun onBind(loadState: LoadState) {
            binding.root.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = holder.onBind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val binding = LayoutSearchUserItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }
}