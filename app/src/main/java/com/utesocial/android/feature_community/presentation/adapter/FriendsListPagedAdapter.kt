package com.utesocial.android.feature_community.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
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
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.databinding.LayoutFriendItemBinding

class FriendsListPagedAdapter(
    private val lifecycleOwner: LifecycleOwner,
) : PagingDataAdapter<User, ViewHolder>(UserListDiffCallback()) {

    class UserListDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun getChangePayload(oldItem: User, newItem: User): Any? {
            if(oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }

    }

    inner class FriendViewHolder(private val binding: LayoutFriendItemBinding) : ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            binding.apply {
                val url = user.avatar?.url
                url?.let {
                    Glide.with(binding.root.context)
                        .load(it)
                        .error(AppCompatResources.getDrawable(binding.root.context, R.drawable.pla_oval))
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
                        .load(AppCompatResources.getDrawable(binding.root.context, R.drawable.pla_oval))
                        .into(imvAvatar)
                }
                txvFullName.text = "${user.firstName} ${user.lastName}"
                txvUsername.text = user.username ?: ""

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as FriendViewHolder).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return FriendViewHolder(
            LayoutFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}