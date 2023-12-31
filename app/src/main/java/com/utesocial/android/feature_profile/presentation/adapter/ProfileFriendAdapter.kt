package com.utesocial.android.feature_profile.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.carousel.MaskableFrameLayout
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraProfileFriendBinding

class ProfileFriendAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<String>
) : Adapter<ProfileFriendAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(private val binding: ItemFraProfileFriendBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        @SuppressLint("RestrictedApi")
        fun bind() {
            (binding.root as MaskableFrameLayout).setOnMaskChangedListener {
                binding.textViewName.translationX = it.left
                binding.textViewName.scaleX = AnimationUtils.lerp(1F, 0F, 0F, 100F, it.left)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendViewHolder = FriendViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_profile_friend, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) = holder.bind()
}