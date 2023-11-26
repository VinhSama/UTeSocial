package com.utesocial.android.feature_notification.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemNotifyBinding

class NotifyAdapter(val data: Array<String>) : Adapter<NotifyAdapter.NotifyViewHolder>() {

    class NotifyViewHolder(binding: ItemNotifyBinding) : ViewHolder(binding.root) {

        init {
            binding.unRead = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val binding: ItemNotifyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notify, parent, false)
        return NotifyViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {

    }
}