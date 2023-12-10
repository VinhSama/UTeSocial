package com.utesocial.android.core.presentation.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBodyImageBinding

class PostBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<String>
) : Adapter<PostBodyImageAdapter.PostBodyImageViewHolder>() {

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(image: String) { binding.image = image }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBodyImageViewHolder = PostBodyImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_body_image, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PostBodyImageViewHolder, position: Int) = holder.bind(data[position])
}