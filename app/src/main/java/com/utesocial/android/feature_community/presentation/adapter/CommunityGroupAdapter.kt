package com.utesocial.android.feature_community.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.animation.AnimationUtils.lerp
import com.google.android.material.carousel.MaskableFrameLayout
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraCommunityGroupBinding
import com.utesocial.android.feature_group.domain.model.Group

class CommunityGroupAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<Group>
) : Adapter<CommunityGroupAdapter.CommunityViewHolder>() {

    inner class CommunityViewHolder(private val binding: ItemFraCommunityGroupBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        @SuppressLint("RestrictedApi")
        fun bind(group: Group) {
            binding.group = group
            (binding.root as MaskableFrameLayout).setOnMaskChangedListener {
                binding.textViewName.translationX = it.left
                binding.textViewName.alpha = lerp(5F, 0F, 0F, 175F, it.left)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder = CommunityViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_community_group, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int)  = holder.bind(data[position])
}