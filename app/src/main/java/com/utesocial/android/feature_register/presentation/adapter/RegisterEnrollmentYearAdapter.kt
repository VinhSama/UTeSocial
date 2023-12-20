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
import com.utesocial.android.feature_register.domain.model.EnrollmentYearRes

class RegisterEnrollmentYearAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<EnrollmentYearRes>,
    private val recyclerView: RecyclerView
) : Adapter<RegisterEnrollmentYearAdapter.RegisterEnrollmentYearViewHolder>() {

    private var currentChecked: Int = 0

    inner class RegisterEnrollmentYearViewHolder(private val binding: ItemDialogChooseBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(enrollmentYearRes: EnrollmentYearRes) {
            binding.text = enrollmentYearRes.name
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
    ): RegisterEnrollmentYearViewHolder = RegisterEnrollmentYearViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dialog_choose, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(
        holder: RegisterEnrollmentYearViewHolder,
        position: Int
    ) = holder.bind(data[position])

    fun getEnrollmentYearSelect(): EnrollmentYearRes = data[currentChecked]

    fun resetData() {
        if (!recyclerView.isComputingLayout) {
            currentChecked = 0
            notifyItemRangeRemoved(0, data.size)
        }
    }
}