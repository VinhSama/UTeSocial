package com.utesocial.android.feature_post.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.databinding.ViewDialogConfirmBinding

class DeletePostDialog(
    layoutInflater: LayoutInflater,
    container: ViewGroup?
) {

    private val bindingDialogDelete: ViewDialogConfirmBinding
    private val dialogDelete: AlertDialog

    init {
        bindingDialogDelete = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.view_dialog_confirm,
            container,
            false
        )

        val materialAlertDialogBuilderDelete = MaterialAlertDialogBuilder(bindingDialogDelete.root.context)
        materialAlertDialogBuilderDelete.setView(bindingDialogDelete.root)

        dialogDelete = materialAlertDialogBuilderDelete.create()
        dialogDelete.window?.attributes?.windowAnimations = R.style.DialogConfirmDelete

        bindingDialogDelete.buttonNeutral.setOnClickListener { dialogDelete.dismiss() }
    }

    fun showDialog(runnable: Runnable) {
        bindingDialogDelete.buttonPositive.setOnClickListener {
            dialogDelete.dismiss()
            runnable.run()
        }

        dialogDelete.show()
    }
}