package com.utesocial.android.feature_change_avatar.presentation.element.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.utesocial.android.R
import com.utesocial.android.databinding.ViewBottomSheetChooseBinding
import com.utesocial.android.feature_change_avatar.presentation.element.ChangeAvatarFragment

class ChangeAvatarBottomSheet(private val changeAvatarFragment: ChangeAvatarFragment) : BottomSheetDialogFragment() {

    private var binding: ViewBottomSheetChooseBinding? = null

    companion object {

        const val TAG = "ChangeAvatarBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_bottom_sheet_choose, container, false)
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
        binding?.lifecycleOwner = this@ChangeAvatarBottomSheet
        binding?.fragment = changeAvatarFragment
    }
}