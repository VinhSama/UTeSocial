package com.utesocial.android.feature_community.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
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
import com.utesocial.android.databinding.LayoutFriendRequestItemBinding
import com.utesocial.android.feature_community.domain.model.FriendRequest

class FriendRequestsPagedAdapter : PagingDataAdapter<FriendRequest, ViewHolder>(FriendRequestDiffCallback()) {
    class FriendRequestDiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
        override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem.requestId == newItem.requestId
        }

        override fun getChangePayload(oldItem: FriendRequest, newItem: FriendRequest): Any? {
            if(oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }
    inner class FriendRequestViewHolder(private val binding: LayoutFriendRequestItemBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(friendRequest: FriendRequest) {
            binding.apply {
                val url = friendRequest.sender.avatar?.url
                url?.let {
                    Glide.with(binding.root.context)
                        .load(it)
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
                } ?: run {
                    Glide.with(binding.root.context)
                        .load(AppCompatResources.getDrawable(binding.root.context, R.drawable.ico_default_profile))
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
                                Handler(Looper.getMainLooper()).postDelayed({
                                    shimmer.stopShimmer()
                                    shimmer.visibility = View.GONE
                                    imvAvatar.visibility = View.VISIBLE
                                }, 300)
                                return false
                            }

                        })
                        .into(imvAvatar)
                }
                txvFullName.text = "${friendRequest.sender.firstName} ${friendRequest.sender.lastName}"
                txvUsername.text = friendRequest.sender.username ?: ""
            }
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as FriendRequestViewHolder).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return FriendRequestViewHolder(
            LayoutFriendRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}