package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.databinding.LayoutCommentItemLoadStateBinding

class CommentLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CommentLoadStateAdapter.LoadStateViewHolder>() {
    inner class LoadStateViewHolder(val binding: LayoutCommentItemLoadStateBinding, retry: () -> Unit) : ViewHolder(binding.root) {
        fun onBind(loadState: LoadState) {
            binding.root.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LayoutCommentItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }
}