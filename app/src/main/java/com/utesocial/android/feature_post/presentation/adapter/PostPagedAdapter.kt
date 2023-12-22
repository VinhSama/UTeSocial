package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.element.partial.PostBody

class PostPagedAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val listener: PostModelBodyImageAdapter.PostBodyImageListener
) : PagingDataAdapter<PostModel, ViewHolder>(PostModelListDiffCallback()) {

    class PostModelListDiffCallback : DiffUtil.ItemCallback<PostModel>() {
        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel) = oldItem == newItem
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel) =
            oldItem.id == newItem.id

        override fun getChangePayload(oldItem: PostModel, newItem: PostModel): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }

    }

    companion object {
        private const val TYPE_PERSONAL = 1
        private const val TYPE_GROUP = 2
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : ViewHolder(binding.root) {
        private val bodyBinding by lazy { PostBody(binding.body) }

        fun bind(post: PostModel) {
            binding.postModel = post
            bodyBinding.setupImages(lifecycleOwner, post, listener = listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position)?.group != null) {
            return TYPE_GROUP
        }
        return TYPE_PERSONAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as PostViewHolder).bind(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val newItem = payloads[0] as PostModel
            (holder as PostViewHolder).bind(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_post,
                parent,
                false
            )
        )
    }
}