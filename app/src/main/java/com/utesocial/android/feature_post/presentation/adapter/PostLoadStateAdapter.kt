package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.databinding.ItemPostLoadStateBinding

class PostLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PostLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: ItemPostLoadStateBinding, retry: () -> Unit) : ViewHolder(binding.root){
        fun onBind(loadState: LoadState) {
            binding.root.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostLoadStateBinding.inflate(inflater, parent, false)
        return LoadStateViewHolder(binding, retry)
    }
}