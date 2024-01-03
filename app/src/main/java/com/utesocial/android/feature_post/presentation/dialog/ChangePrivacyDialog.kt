package com.utesocial.android.feature_post.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.databinding.ViewDialogScopeBinding

class ChangePrivacyDialog(
    layoutInflater: LayoutInflater,
    container: ViewGroup?
) {

    private val bindingDialogScope: ViewDialogScopeBinding
    private val dialogScope: AlertDialog

    init {
        bindingDialogScope = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.view_dialog_scope,
            container,
            false
        )

        val materialAlertDialogBuilderScope = MaterialAlertDialogBuilder(bindingDialogScope.root.context)
        materialAlertDialogBuilderScope.setView(bindingDialogScope.root)

        dialogScope = materialAlertDialogBuilderScope.create()
        dialogScope.window?.attributes?.windowAnimations = R.style.DialogChooseScope

        bindingDialogScope.buttonNeutral.setOnClickListener { dialogScope.dismiss() }
    }

    fun showDialog(runnable: Runnable) {
        bindingDialogScope.buttonPositive.setOnClickListener {
            dialogScope.dismiss()
            runnable.run()
        }

        dialogScope.show()
    }

    fun setDefaultChecked(privacyMode: Int) = when (privacyMode) {
        0 -> bindingDialogScope.radioButtonPrivate.isChecked = true
        2 -> bindingDialogScope.radioButtonFriend.isChecked = true
        else -> bindingDialogScope.radioButtonPublic.isChecked = true
    }

    fun getPrivacySelected() = if (bindingDialogScope.radioButtonPrivate.isChecked) 0
    else if (bindingDialogScope.radioButtonPublic.isChecked) 1
    else 2
}