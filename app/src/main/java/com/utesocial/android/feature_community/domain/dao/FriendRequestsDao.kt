package com.utesocial.android.feature_community.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utesocial.android.feature_community.domain.model.FriendRequestEntity

@Dao
interface FriendRequestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendRequests: List<FriendRequestEntity>)

    @Query("Select * from FriendRequestEntity")
    fun getRequestsList() : PagingSource<Int, FriendRequestEntity>

    @Query("Delete from FriendRequestEntity")
    suspend fun clearAll()

    @Update
    suspend fun updateRequestAfterResponse(friendRequest: FriendRequestEntity)

}