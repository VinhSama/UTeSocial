package com.utesocial.android.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBinding

class HomeAdapter(val data: Array<String>) : Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding: ItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

    }

    class HomeViewHolder(val binding: ItemPostBinding) : ViewHolder(binding.root) {

        init {
            binding.interact.radioButtonComment.setOnClickListener {
                binding.interact.radioButtonComment.isChecked = false
            }
        }
    }
}