package com.utesocial.android.core.presentation.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    protected abstract var binding: DB
    protected abstract val viewModel: ViewModel?

    private lateinit var activity: BaseActivity<*>

    protected abstract fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as BaseActivity<*>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initDataBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel

        binding.lifecycleOwner = this@BaseFragment
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun hideKeyboard() {
        binding.root.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == ACTION_UP) {
                val viewFocus = view?.findFocus()
                if (viewFocus != null) {
                    activity.handleHideKeyboard(viewFocus)
                }
            }
            false
        }
    }

    fun getBaseActivity(): BaseActivity<*> = activity
}