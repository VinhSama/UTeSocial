package com.utesocial.android.feature_notification.presentation.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.core.presentation.util.OverlapAvatarItemDecoration
import com.utesocial.android.databinding.ItemFraRequestBinding
import com.utesocial.android.feature_notification.domain.model.Request

class RequestAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<Request>
) : Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(private val binding: ItemFraRequestBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(request: Request) {
            binding.request = request

            val numberLimit = 3
            val requestFriendAdapter = RequestFriendAdapter(lifecycleOwner, request.mutualFriends, numberLimit)

            binding.recyclerViewFriend.addItemDecoration(OverlapAvatarItemDecoration(numberLimit))
            binding.recyclerViewFriend.adapter = requestFriendAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder = RequestViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_request, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) = holder.bind(data[position])
}