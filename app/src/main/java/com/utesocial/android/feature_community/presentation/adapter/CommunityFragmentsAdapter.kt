package com.utesocial.android.feature_community.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.utesocial.android.feature_community.presentation.friendList.element.FriendsListFragment
import com.utesocial.android.feature_community.presentation.friendRequest.element.FriendRequestFragment

class CommunityFragmentsAdapter(
    fragmentManager: FragmentManager,
    viewLifecycleOwner: LifecycleOwner
) : FragmentStateAdapter(fragmentManager, viewLifecycleOwner.lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) {
            FriendsListFragment()
        } else {
            FriendRequestFragment()
        }
    }
}