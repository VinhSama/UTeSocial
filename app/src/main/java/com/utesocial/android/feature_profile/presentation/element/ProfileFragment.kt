package com.utesocial.android.feature_profile.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.transition.ChangeBounds
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.databinding.FragmentProfileBinding
import com.utesocial.android.feature_profile.presentation.adapter.ProfileFriendAdapter

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override lateinit var binding: FragmentProfileBinding
    override val viewModel: ViewModel? = null

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })

    /*private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.linearLayoutPosts) }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            bottomSheetBehavior.state = STATE_COLLAPSED
            isEnabled = false
        }
    }
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == STATE_EXPANDED) {
                onBackPressedCallback.isEnabled = true
                getBaseActivity().onBackPressedDispatcher.addCallback(this@ProfileFragment, onBackPressedCallback)
            } else {
                onBackPressedCallback.isEnabled = false
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    private fun setup() { bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback) }

    override fun onDestroyView() {
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
        super.onDestroyView()
    }*/

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupRecyclerView()
        setupListener()
    }

    private fun setupBinding() { binding.mainViewModel = mainViewModel }

    private fun setupRecyclerView() {
//        val profileFriendAdapter = ProfileFriendAdapter(this@ProfileFragment, arrayListOf("", "", ""))
//        binding.friend.recyclerViewFriend.adapter = profileFriendAdapter
    }

    private fun setupListener() {
        binding.info.cardViewInfo.setOnClickListener { navigationDetail() }
        binding.info.buttonMore.setOnClickListener { navigationDetail() }
    }

    private fun navigationDetail() {
        val action = ProfileFragmentDirections.actionProfileDetail()
        getBaseActivity().navController()?.navigate(action)
    }
}