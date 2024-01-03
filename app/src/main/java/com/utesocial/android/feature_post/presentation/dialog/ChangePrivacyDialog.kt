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
import com.utesocial.android.databinding.ViewDialogScopeBinding
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.remote.networkState.Status
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse

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

    fun showDialog(
        fragment: BaseFragment<*>,
        liveData: LiveData<SimpleResponse<AppResponse<PrivacyResponse>?>>,
        runnable: Runnable
    ) {
//        bindingDialogScope.buttonPositive.setOnClickListener {
//            dialogScope.dismiss()
//
//            val privacySelect = if (bindingDialogScope.radioButtonPrivate.isChecked) {
//                0
//            } else if (bindingDialogScope.radioButtonPublic.isChecked) {
//                1
//            } else {
//                2
//            }
//
//            viewModel.changePrivacy(postId, PrivacyRequest(privacySelect))
//                .observe(viewLifecycleOwner) { responseState ->
//                    when (responseState.getNetworkState().getStatus()) {
//                        Status.RUNNING -> showLoadingDialog()
//                        Status.SUCCESS -> {
//                            dismissLoadingDialog()
//                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_scope_success))
//                            infoBinding.setPrivacy(privacySelect)
//                        }
//
//                        Status.FAILED -> {
//                            dismissLoadingDialog()
//                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_scope_fail))
//                        }
//
//                        else -> return@observe
//                    }
//                }
//        }
//
//        when (privacyMode) {
//            0 -> bindingDialogScope.radioButtonPrivate.isChecked = true
//            1 -> bindingDialogScope.radioButtonPublic.isChecked = true
//            2 -> bindingDialogScope.radioButtonFriend.isChecked = true
//        }
//
//        dialogScope.show()
    }
}