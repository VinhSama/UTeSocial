package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraPostBinding

class PostImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<String>
) : Adapter<PostImageAdapter.PostImageViewHolder>() {

    inner class PostImageViewHolder(private val binding: ItemFraPostBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(image: String) { binding.image = image }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImageViewHolder = PostImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_post, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PostImageViewHolder, position: Int) = holder.bind(data[position])
}