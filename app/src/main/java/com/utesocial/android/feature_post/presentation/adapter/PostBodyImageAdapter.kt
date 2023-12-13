package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener
import com.utesocial.android.databinding.ItemPostBodyImageBinding

class PostBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: Post,
    private val listener: PostBodyImageListener
) : Adapter<PostBodyImageAdapter.PostBodyImageViewHolder>() {

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(post: Post, position: Int) {
            binding.image = post.images[position]
            binding.imageViewContent.setOnClickListener { listener.onClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBodyImageViewHolder = PostBodyImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_body_image, parent, false))

    override fun getItemCount(): Int = data.images.size

    override fun onBindViewHolder(holder: PostBodyImageViewHolder, position: Int) = holder.bind(data, position)
}