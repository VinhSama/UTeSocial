package com.utesocial.android.feature_community.presentation.friendList.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentFriendListBinding
import com.utesocial.android.feature_community.presentation.state_holder.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsListFragment : BaseFragment<FragmentFriendListBinding>() {

    override lateinit var binding: FragmentFriendListBinding
    override val viewModel : CommunityViewModel by viewModels()

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFriendListBinding {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, false)
        binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.setOnRefreshListener {
            Debug.log("asdasd", "asdas")
        }
    }
}