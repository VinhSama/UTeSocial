package com.utesocial.android.feature_notification.presentation.notify.element.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.utesocial.android.R
import com.utesocial.android.databinding.ViewBottomSheetNotifyBinding
import com.utesocial.android.feature_notification.domain.model.Notify

class NotifyBottomSheet : BottomSheetDialogFragment() {

    private var binding: ViewBottomSheetNotifyBinding? = null

    private lateinit var notify: Notify

    companion object {

        const val TAG = "NotifyBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_bottom_sheet_notify, container, false)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupBinding() {
        binding?.lifecycleOwner = this@NotifyBottomSheet
        binding?.notify = notify
    }

    fun bindNotify(notify: Notify) { this.notify = notify }
}