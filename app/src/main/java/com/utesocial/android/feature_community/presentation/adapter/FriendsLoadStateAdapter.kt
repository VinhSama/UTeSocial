package com.utesocial.android.feature_community.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.databinding.LayoutFriendItemLoadStateBinding

class FriendsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FriendsLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: LayoutFriendItemLoadStateBinding, retry: () -> Unit) : ViewHolder(binding.root) {
        fun onBind(loadState: LoadState) {
            binding.root.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LayoutFriendItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }
}