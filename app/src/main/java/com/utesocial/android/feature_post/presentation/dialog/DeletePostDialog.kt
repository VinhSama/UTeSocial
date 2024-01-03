package com.utesocial.android.feature_post.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.ViewDialogConfirmBinding
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.networkState.Status
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse

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

    fun showDialog(
        fragment: BaseFragment<*>,
        liveData: LiveData<SimpleResponse<AppResponse<Void>?>>,
        runnable: Runnable
    ) {
        bindingDialogDelete.buttonPositive.setOnClickListener {
            dialogDelete.dismiss()
            val baseActivity = fragment.getBaseActivity()

            liveData.observe(fragment.viewLifecycleOwner) { responseState ->
                when (responseState.getNetworkState().getStatus()) {
                    Status.RUNNING -> fragment.showLoadingDialog()

                    Status.SUCCESS -> {
                        dismissLoadingDialog()
                        baseActivity.showSnackbar(message = baseActivity.getString(R.string.str_dialog_confirm_delete_post_success))
                        runnable.run()
                    }

                    Status.FAILED -> {
                        dismissLoadingDialog()
                        baseActivity.showSnackbar(message = baseActivity.getString(R.string.str_dialog_confirm_delete_post_fail))
                    }

                    else -> return@observe
                }
            }
        }

        dialogDelete.show()
    }
}