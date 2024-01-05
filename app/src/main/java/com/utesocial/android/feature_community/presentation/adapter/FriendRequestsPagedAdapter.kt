package com.utesocial.android.feature_community.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
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
import com.utesocial.android.feature_community.domain.model.FriendRequestEntity
import kotlinx.coroutines.withContext

class FriendRequestsPagedAdapter(
    val requestItemOnActions: RequestItemOnActions
) : PagingDataAdapter<FriendRequestEntity, ViewHolder>(FriendRequestDiffCallback()) {
    class FriendRequestDiffCallback : DiffUtil.ItemCallback<FriendRequestEntity>() {
        override fun areItemsTheSame(oldItem: FriendRequestEntity, newItem: FriendRequestEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FriendRequestEntity, newItem: FriendRequestEntity): Boolean {
            return oldItem.requestId == newItem.requestId
        }

        override fun getChangePayload(oldItem: FriendRequestEntity, newItem: FriendRequestEntity): Any? {
            if(oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }
    inner class FriendRequestViewHolder(private val binding: LayoutFriendRequestItemBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(friendRequestEntity: FriendRequestEntity) {
            binding.apply {
                val url = friendRequestEntity.avatar?.url
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
                txvFullName.text = friendRequestEntity.fullName
                txvUsername.text = friendRequestEntity.username ?: ""

                viewGroupBtnActions.isVisible = friendRequestEntity.status == FriendRequest.FriendState.PENDING
                txvRespondNotify.isVisible = friendRequestEntity.status != FriendRequest.FriendState.PENDING
                when(friendRequestEntity.status) {
                    FriendRequest.FriendState.ACCEPTED -> {
                        root.context?.apply {
                            txvRespondNotify.text = getString(R.string.str_accept_friend_request_notify)
                        }
                    }
                    FriendRequest.FriendState.REJECTED -> {
                        root.context?.apply {
                            txvRespondNotify.text = getString(R.string.str_remove_friend_request_notify)
                        }
                    }
                    else -> {}
                }
                btnAccept.setOnClickListener {
                    requestItemOnActions.onAcceptClick(friendRequestEntity)
                }
                btnDeny.setOnClickListener {
                    requestItemOnActions.onDenyClick(friendRequestEntity)
                }
                root.setOnClickListener {
                    requestItemOnActions.onProfileClick(friendRequestEntity)
                }
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

    interface RequestItemOnActions {
        fun onAcceptClick(request: FriendRequestEntity)
        fun onDenyClick(request: FriendRequestEntity)
        fun onProfileClick(request: FriendRequestEntity)
    }
}