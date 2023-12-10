package com.utesocial.android.feature_notification.presentation.notify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraNotifyBinding
import com.utesocial.android.feature_notification.domain.model.Notify
import com.utesocial.android.feature_notification.presentation.notify.listener.NotifyItemListener

class NotifyAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<Notify>,
    private val listener: NotifyItemListener
) : Adapter<NotifyAdapter.NotifyViewHolder>() {

    inner class NotifyViewHolder(private val binding: ItemFraNotifyBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(notify: Notify) {
            binding.notify = notify
            binding.listener = listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder = NotifyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_notify, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) = holder.bind(data[position])
}