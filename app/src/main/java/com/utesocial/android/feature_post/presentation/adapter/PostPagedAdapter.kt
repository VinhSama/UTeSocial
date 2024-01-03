package com.utesocial.android.feature_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.element.partial.InfoItemPost
import com.utesocial.android.feature_post.presentation.element.partial.PostBody
import com.utesocial.android.feature_post.presentation.listener.PostListener

class PostPagedAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val listener: PostListener,
    private val currentUserId : String?,
    private val onItemActionsListener: OnItemActionsListener
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

        private val infoBinding by lazy { InfoItemPost(binding.info) }
        private val bodyBinding by lazy { PostBody(binding.body) }

        fun bind(post: PostModel) {
            val userAuthorId = post.userAuthor?.id ?: ""
            if (currentUserId != userAuthorId) {
                infoBinding.hideButtonMenu()
            } else {
                infoBinding.setupListener(
                    listener = listener,
                    postId = post.id
                )
            }

            binding.postModel = post
            binding.txvLikeInfoHeader.text = post.likeCounts.toString()
            var textLikeHeader = "${post.likeCounts}"
            var liked = false
            var friendName = ""
            currentUserId?.let {
                post.likes.forEachIndexed { index, likesPostHeader ->
                    if(!likesPostHeader.isFriend ) {
                        (likesPostHeader.userId == currentUserId).run {
                            liked = true
                        }
                    } else {
                        friendName = likesPostHeader.fullName
                    }
                }
            }
            if(friendName.isNotEmpty()) {
                if(liked) {
                    val count = post.likeCounts - 2
                    textLikeHeader = "Bạn, $friendName"
                    if(count > 0) {
                        textLikeHeader = "$textLikeHeader và $count người khác"
                    }
                } else {
                    val count = post.likeCounts - 1
                    textLikeHeader = friendName

                    if(count > 0) {
                        textLikeHeader = "$textLikeHeader và $count người khác"
                    }
                }
            } else {
                if(liked) {
                    textLikeHeader = if(post.likeCounts - 1 > 0) {
                        "Bạn và ${post.likeCounts - 1} người khác"
                    } else {
                        "Bạn"
                    }
                }
            }
            binding.txvLikeInfoHeader.text = textLikeHeader
            binding.btnLike.isChecked = liked
//            binding.btnLike.setOnClickListener {
//                (it as MaterialRadioButton).apply {
//                    isChecked = !isChecked
//
//                }
//            }
            binding.btnLike.setOnCheckedChangeListener(null)
            binding.btnLike.setOnClickListener {
                val isChecked = (it as MaterialCheckBox).isChecked
                if(!onItemActionsListener.onLikeChanged(isChecked, post)) {
                    it.isChecked = !isChecked
                }
//                onItemActionsListener.onLikeChanged(binding.btnLike.isChecked, post)
            }

//            binding.btnLike.setOnCheckedChangeListener { _, isChecked ->
//                onItemActionsListener.onLikeChanged(isChecked, post)
//            }
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

    interface OnItemActionsListener {
        fun onLikeChanged(isChecked: Boolean, postModel: PostModel) : Boolean
    }
}