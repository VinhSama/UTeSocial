package com.utesocial.android.feature_community.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utesocial.android.feature_community.domain.model.FriendRequest

@Dao
interface FriendRequestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendRequests: List<FriendRequest>)

    @Query("Select * from FriendRequest")
    fun getRequestsList() : PagingSource<Int, FriendRequest>

    @Query("Delete from FriendRequest")
    suspend fun clearAll()

    @Update
    suspend fun updateRequestAfterResponse(friendRequest: FriendRequest)

}