package com.utesocial.android.feature_post.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_post.domain.model.PostRemoteKeys

@Dao
interface PostRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<PostRemoteKeys>)
    @Query("Select * from PostRemoteKeys where postId = :id")
    suspend fun getRemoteKeys(id: String) : PostRemoteKeys
    @Query("Delete from PostRemoteKeys")
    suspend fun clearAll()
}