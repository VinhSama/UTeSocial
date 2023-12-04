package com.utesocial.android.feature_notification.presentation.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.core.presentation.util.OverlapAvatarItemDecoration
import com.utesocial.android.databinding.ItemFraRequestBinding

class RequestAdapter(val data: Array<String>) : Adapter<RequestAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding: ItemFraRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_request, parent, false)
        return RequestViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {

    }

    class RequestViewHolder(val binding: ItemFraRequestBinding) : ViewHolder(binding.root) {

        init {
            val friendAdapter = RequestFriendAdapter()
            binding.recyclerViewFriend.addItemDecoration(OverlapAvatarItemDecoration(3, -20))
            binding.recyclerViewFriend.adapter = friendAdapter
        }
    }
}