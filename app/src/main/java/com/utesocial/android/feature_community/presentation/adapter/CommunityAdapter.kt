package com.utesocial.android.feature_community.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraCommunityBinding
import com.utesocial.android.feature_community.domain.model.Community

class CommunityAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<Community>
) : Adapter<CommunityAdapter.CommunityViewHolder>() {

    inner class CommunityViewHolder(private val binding: ItemFraCommunityBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(community: Community) {
            binding.image = community.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder = CommunityViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_community, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) = holder.bind(data[position])
}