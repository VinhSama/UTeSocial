package com.utesocial.android.feature_notification.presentation.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraRequestAvatarBinding

class RequestFriendAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<String>,
    private val numberLimit: Int
) : Adapter<RequestFriendAdapter.RequestFriendViewHolder>() {

    inner class RequestFriendViewHolder(val binding: ItemFraRequestAvatarBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(avatar: String) { binding.avatar = avatar }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestFriendViewHolder = RequestFriendViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_request_avatar, parent, false))

    override fun getItemCount(): Int = if (numberLimit < data.size) {
        numberLimit
    } else {
        data.size
    }

    override fun onBindViewHolder(holder: RequestFriendViewHolder, position: Int) = holder.bind(data[position])
}