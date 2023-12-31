package com.utesocial.android.feature_community.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_community.domain.model.FriendsListRemoteKeys

@Dao
interface FriendsRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<FriendsListRemoteKeys>)

    @Query("Select * from FriendsListRemoteKeys where userId = :id")
    suspend fun getRemoteKeys(id: String) : FriendsListRemoteKeys

    @Query("Delete from FriendsListRemoteKeys")
    suspend fun clearAll()
}