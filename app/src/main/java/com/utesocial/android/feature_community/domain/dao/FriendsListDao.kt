package com.utesocial.android.feature_community.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.utesocial.android.core.domain.model.User
@Dao
interface FriendsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friendsList: List<User>) : List<User>
    @Upsert
    suspend fun upsertAll(friendsList: List<User>) : List<User>

    @Query("Select * from User")
    fun getFriendsList() : PagingSource<Int, User>

    @Query("Delete from User")
    suspend fun clearAll()
}