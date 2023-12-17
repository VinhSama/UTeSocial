package com.utesocial.android.feature_settings.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override lateinit var binding: FragmentSettingsBinding
    override val viewModel = null

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingsBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding
    }
}