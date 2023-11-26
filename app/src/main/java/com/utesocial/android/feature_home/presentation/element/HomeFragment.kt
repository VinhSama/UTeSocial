package com.utesocial.android.feature_home.presentation.element

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding
    }

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@HomeFragment }

    private var isShow = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.setOnClickListener {
            getBaseActivity().handleBar(isShow)
            isShow = !isShow
            Log.d("VVVVV", "Show: " + isShow)
        }
    }
}