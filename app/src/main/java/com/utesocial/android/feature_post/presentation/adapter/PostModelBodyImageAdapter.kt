package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBodyImageBinding
import com.utesocial.android.feature_post.domain.model.PostResource

class PostModelBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val postResources: List<PostResource>,
    private val listener: PostBodyImageListener
) : RecyclerView.Adapter<PostModelBodyImageAdapter.PostBodyImageViewHolder>() {

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(postResources: List<PostResource>, position: Int) {
            binding.image = postResources[position].url
            binding.imageViewContent.setOnClickListener { listener.onClick(postResources[position]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBodyImageViewHolder {
        return PostBodyImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_body_image, parent, false))
    }

    override fun getItemCount(): Int = postResources.size

    override fun onBindViewHolder(holder: PostBodyImageViewHolder, position: Int) {
        holder.bind(postResources, position)
    }

    interface PostBodyImageListener {
        fun onClick(postResource: PostResource)
    }
}