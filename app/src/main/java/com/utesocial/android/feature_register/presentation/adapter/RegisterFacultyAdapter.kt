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
import com.utesocial.android.feature_register.domain.model.FacultyRes

class RegisterFacultyAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<FacultyRes>,
    private val recyclerView: RecyclerView
) : Adapter<RegisterFacultyAdapter.RegisterFacultyViewHolder>() {

    private var currentChecked: Int = 0

    inner class RegisterFacultyViewHolder(private val binding: ItemDialogChooseBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(facultyRes: FacultyRes) {
            binding.text = facultyRes.name + " (" + facultyRes.code + ")"
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
    ): RegisterFacultyViewHolder = RegisterFacultyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dialog_choose, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(
        holder: RegisterFacultyViewHolder,
        position: Int
    ) = holder.bind(data[position])

    fun getFacultySelect(): FacultyRes = data[currentChecked]

    fun resetData() {
        if (!recyclerView.isComputingLayout) {
            currentChecked = 0
            notifyItemRangeRemoved(0, data.size)
        }
    }
}