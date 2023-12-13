package com.utesocial.android.feature_community.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraCommunityBinding
import com.utesocial.android.databinding.ItemPostBinding
import com.utesocial.android.feature_group.domain.model.Group
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.feature_post.presentation.element.partial.InfoItemPost
import com.utesocial.android.feature_post.presentation.element.partial.PostBody
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener

class CommunityAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val dataGroup: List<Group>,
    private val dataPost: List<Post>,
    private val listener: PostBodyImageListener
) : Adapter<ViewHolder>() {

    companion object {

        private const val TYPE_GROUP = 1
        private const val TYPE_POST = 2
    }

    inner class GroupViewHolder(private val binding: ItemFraCommunityBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(groups: List<Group>) {
            binding.isEmpty = groups.isEmpty()

            val communityGroupAdapter = CommunityGroupAdapter(lifecycleOwner, data = groups)
            binding.recyclerViewGroup.adapter = communityGroupAdapter
        }
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : ViewHolder(binding.root) {

        private val bodyBinding by lazy { PostBody(binding.body) }

        init { binding.lifecycleOwner =  lifecycleOwner}

        fun bind(post: Post) {
            binding.post = post
            bodyBinding.setupImages(lifecycleOwner, post, listener)

            InfoItemPost(binding.info)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = if (viewType == TYPE_GROUP) {
        GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_community, parent, false))
    } else {
        PostViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false))
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) {
        TYPE_GROUP
    } else {
        TYPE_POST
    }

    override fun getItemCount(): Int = dataPost.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = if (position == 0) {
        (holder as GroupViewHolder).bind(dataGroup)
    } else {
        (holder as PostViewHolder).bind(dataPost[position - 1])
    }
}