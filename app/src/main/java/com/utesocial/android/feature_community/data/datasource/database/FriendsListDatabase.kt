package com.utesocial.android.feature_community.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_community.domain.dao.FriendsListDao
import com.utesocial.android.feature_community.domain.dao.FriendsRemoteKeysDao
import com.utesocial.android.feature_community.domain.model.FriendsListRemoteKeys

@Database(
    entities = [User::class, FriendsListRemoteKeys::class],
    version = 1
)
abstract class FriendsListDatabase : RoomDatabase() {
    abstract fun getFriendsListDao() : FriendsListDao
    abstract fun getRemoteKeysDao() : FriendsRemoteKeysDao
}