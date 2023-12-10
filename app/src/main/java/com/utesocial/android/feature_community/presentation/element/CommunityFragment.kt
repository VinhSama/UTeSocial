package com.utesocial.android.feature_community.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentCommunityBinding

class CommunityFragment : BaseFragment() {

    private lateinit var binding: FragmentCommunityBinding

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)
        return binding
    }

    override fun initViewModel(): ViewModel? = null

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@CommunityFragment }

    private var isShow = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener {
            getBaseActivity().handleBottomBar(isShow)
            isShow = !isShow
        }
    }
}