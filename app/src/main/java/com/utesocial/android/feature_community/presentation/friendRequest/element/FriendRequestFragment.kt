package com.utesocial.android.feature_community.presentation.friendRequest.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentFriendRequestBinding
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendRequestFragment : BaseFragment<FragmentFriendRequestBinding>() {
    override lateinit var binding: FragmentFriendRequestBinding
    override val viewModel: CommunityViewModel by viewModels()
    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFriendRequestBinding {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}