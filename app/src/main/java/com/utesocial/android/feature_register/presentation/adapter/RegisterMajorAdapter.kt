package com.utesocial.android.feature_register.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemDialogChooseBinding
import com.utesocial.android.feature_register.domain.model.MajorRes

class RegisterMajorAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<MajorRes>,
    private val recyclerView: RecyclerView
) : Adapter<RegisterMajorAdapter.RegisterMajorViewHolder>() {

    private var currentChecked: Int = 0

    inner class RegisterMajorViewHolder(private val binding: ItemDialogChooseBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(majorRes: MajorRes) {
            binding.text = majorRes.name["vi"] + " (" + majorRes.code + ")"
            binding.radioButtonOption.isChecked = adapterPosition == currentChecked

            binding.radioButtonOption.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && !recyclerView.isComputingLayout) {
                    currentChecked = adapterPosition
                    notifyItemRangeChanged(0, data.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegisterMajorViewHolder = RegisterMajorViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dialog_choose, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(
        holder: RegisterMajorViewHolder,
        position: Int
    ) = holder.bind(data[position])

    fun getMajorSelect(): MajorRes = data[currentChecked]

    fun resetData() {
        if (!recyclerView.isComputingLayout) {
            currentChecked = 0
            notifyItemRangeRemoved(0, data.size)
        }
    }
}