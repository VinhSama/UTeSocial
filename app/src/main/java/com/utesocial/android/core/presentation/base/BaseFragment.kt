package com.utesocial.android.core.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var binding: ViewDataBinding? = null
    private lateinit var activity: BaseActivity

    abstract fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding

    abstract fun assignLifecycleOwner()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as BaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initDataBinding(inflater, container, savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        assignLifecycleOwner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun getBaseActivity(): BaseActivity = activity



    /**
    protected fun requestShowKeyboard(view: View) = activity.showKeyboard(view)

    protected fun requestHideKeyboard() = activity.hideKeyboard()

    initDataBinding(inflater, container, savedInstanceState).setOnTouchListener { view, motionEvent ->
        activity.hideKeyboard()
        false
    }
    */
}