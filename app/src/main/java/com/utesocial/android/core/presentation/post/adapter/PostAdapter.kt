package com.utesocial.android.core.presentation.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.core.domain.model.Post
import com.utesocial.android.core.presentation.post.element.partial.PostBody
import com.utesocial.android.databinding.ItemGroupBinding
import com.utesocial.android.databinding.ItemPostBinding

class PostAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<Post>
) : Adapter<ViewHolder>() {

    companion object {

        private const val TYPE_PERSONAL = 1
        private const val TYPE_GROUP = 2
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : ViewHolder(binding.root) {

        private val bodyBinding by lazy { PostBody(binding.body) }

        init { binding.lifecycleOwner =  lifecycleOwner}

        fun bind(post: Post) {
            binding.post = post
            bodyBinding.setupImages(lifecycleOwner, post.images)
        }
    }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) : ViewHolder(binding.root) {

        private val bodyBinding by lazy { PostBody(binding.body) }

        init { binding.lifecycleOwner =  lifecycleOwner}

        fun bind(post: Post) {
            binding.group = post
            bodyBinding.setupImages(lifecycleOwner, post.images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = if (viewType == TYPE_PERSONAL) {
        PostViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false))
    } else {
        GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_group, parent, false))
    }

    override fun getItemViewType(position: Int): Int = if (data[position].isPersonal) {
        TYPE_PERSONAL
    } else {
        TYPE_GROUP
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = data[position]
        if (post.isPersonal) {
            (holder as PostViewHolder).bind(post)
        } else {
            (holder as GroupViewHolder).bind(post)
        }
    }
}