package com.utesocial.android.core.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemSearchBinding

class SearchAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<String>
) : Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemSearchBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(string: String) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder = SearchViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) = holder.bind(data[position])
}