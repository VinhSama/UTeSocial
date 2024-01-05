package com.utesocial.android.feature_post.presentation.adapter

import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Common
import com.utesocial.android.databinding.LayoutCommentItemBinding
import com.utesocial.android.feature_post.domain.model.Comment

class CommentPagedAdapter : PagingDataAdapter<Comment, ViewHolder>(CommentListDiffCallback()){

    class CommentListDiffCallback : DiffUtil.ItemCallback<Comment>() {

        override fun getChangePayload(oldItem: Comment, newItem: Comment): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = oldItem == newItem

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.commentId == newItem.commentId
        }

    }

    inner class CommentViewHolder(private val binding: LayoutCommentItemBinding) : ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.apply {
                txvFullName.text = comment.fullName
                txvCommentContent.text = comment.text
                txvCreatedAt.text = Common.getTimeAgo(comment.createdAt, binding.root.context)
                Glide.with(binding.root.context)
                    .load(comment.avatar)
                    .error(AppCompatResources.getDrawable(binding.root.context, R.drawable.ico_default_profile))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE
                            imvAvatar.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Handler().postDelayed(Runnable {
                                shimmer.stopShimmer()
                                shimmer.visibility = View.GONE
                                imvAvatar.visibility = View.VISIBLE
                            }, 300)
                            return false
                        }

                    })
                    .into(imvAvatar)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as CommentViewHolder).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CommentViewHolder(
            LayoutCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }
}