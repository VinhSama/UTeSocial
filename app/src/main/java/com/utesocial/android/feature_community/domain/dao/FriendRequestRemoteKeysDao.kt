package com.utesocial.android.feature_community.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_community.domain.model.FriendRequestRemoteKeys

@Dao
interface FriendRequestRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<FriendRequestRemoteKeys>)

    @Query("Select * from FriendRequestRemoteKeys where requestId = :id")
    suspend fun getRemoteKeys(id: String) : FriendRequestRemoteKeys

    @Query("Delete from FriendRequestRemoteKeys")
    suspend fun clearAll()
}