package com.utesocial.android.feature_community.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.util.Converters
import com.utesocial.android.feature_community.domain.dao.FriendRequestRemoteKeysDao
import com.utesocial.android.feature_community.domain.dao.FriendRequestsDao
import com.utesocial.android.feature_community.domain.dao.FriendsListDao
import com.utesocial.android.feature_community.domain.dao.FriendsRemoteKeysDao
import com.utesocial.android.feature_community.domain.model.FriendRequest
import com.utesocial.android.feature_community.domain.model.FriendRequestRemoteKeys
import com.utesocial.android.feature_community.domain.model.FriendsListRemoteKeys

@Database(
    entities = [User::class, FriendsListRemoteKeys::class, FriendRequest::class, FriendRequestRemoteKeys::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class CommunityDatabase : RoomDatabase() {
    abstract fun getFriendsListDao() : FriendsListDao
    abstract fun getRemoteKeysDao() : FriendsRemoteKeysDao
    abstract fun getRequestRemoteKeysDao() : FriendRequestRemoteKeysDao
    abstract fun getFriendRequestsDao() : FriendRequestsDao
}