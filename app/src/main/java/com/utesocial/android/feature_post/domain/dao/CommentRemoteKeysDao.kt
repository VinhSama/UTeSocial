package com.utesocial.android.feature_post.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_post.domain.model.CommentRemoteKeys

@Dao
interface CommentRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<CommentRemoteKeys>)
    @Query("Select * from CommentRemoteKeys where commentId = :id")
    suspend fun getRemoteKeys(id: String) : CommentRemoteKeys
    @Query("Delete from CommentRemoteKeys")
    suspend fun clearAll()
}