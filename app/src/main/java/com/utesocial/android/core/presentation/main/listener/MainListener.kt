package com.utesocial.android.core.presentation.main.listener

import com.utesocial.android.feature_search.domain.model.SearchUser

interface MainListener {

    fun onSendFriendRequest(receiverId: String)
    fun onSendFriendRequest(searchUser: SearchUser)

    fun onProfileClick(searchUser: SearchUser)
}