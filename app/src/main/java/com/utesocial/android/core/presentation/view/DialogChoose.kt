package com.utesocial.android.core.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.DialogCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.databinding.ViewDialogChooseBinding

class DialogChoose(parent: ViewGroup) {

    val binding: ViewDialogChooseBinding
    private val dialog: AlertDialog

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_dialog_choose, parent, false)
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(binding.root.context)
        materialAlertDialogBuilder.setView(binding.root)
        dialog = materialAlertDialogBuilder.create()
    }

    fun setData(dialogChooseData: DialogChooseData) { binding.data = dialogChooseData }

    fun setList(adapter: Adapter<*>) {
        binding.recyclerViewList.adapter = adapter
    }

    fun setPositiveButtonClickListener(onClickListener: View.OnClickListener) = binding.buttonPositive.setOnClickListener(onClickListener)

    fun setNeutralButtonClickListener(onClickListener: View.OnClickListener) = binding.buttonNeutral.setOnClickListener(onClickListener)

    fun showDialog() = dialog.show()

    fun dismissDialog() = dialog.dismiss()
}

data class DialogChooseData(
    val title: String,
    val content: String,
    val positive: String,
    val neutral: String
)