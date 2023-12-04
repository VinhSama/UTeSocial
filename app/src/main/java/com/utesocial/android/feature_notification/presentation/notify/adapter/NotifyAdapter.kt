package com.utesocial.android.feature_notification.presentation.notify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraNotifyBinding
import com.utesocial.android.feature_notification.presentation.notify.listener.NotifyItemListener

class NotifyAdapter(val data: Array<Boolean>, val notifyItemListener: NotifyItemListener) : Adapter<NotifyAdapter.NotifyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val binding: ItemFraNotifyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_notify, parent, false)
        return NotifyViewHolder(binding, notifyItemListener)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        holder.setB(data[position])
    }

    class NotifyViewHolder(val binding: ItemFraNotifyBinding, val notifyItemListener: NotifyItemListener) : ViewHolder(binding.root) {

        fun setB(unRead: Boolean) {
            binding.root.setOnLongClickListener {
                notifyItemListener.onLongClick()
                true
            }
        }
    }
}