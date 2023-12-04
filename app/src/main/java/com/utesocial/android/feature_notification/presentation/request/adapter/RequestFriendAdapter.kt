package com.utesocial.android.feature_notification.presentation.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraRequestAvatarBinding

class RequestFriendAdapter : Adapter<RequestFriendAdapter.RequestFriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestFriendViewHolder {
        val binding: ItemFraRequestAvatarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_request_avatar, parent, false)
        return RequestFriendViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: RequestFriendViewHolder, position: Int) {

    }

    class RequestFriendViewHolder(val binding: ItemFraRequestAvatarBinding) : ViewHolder(binding.root)
}