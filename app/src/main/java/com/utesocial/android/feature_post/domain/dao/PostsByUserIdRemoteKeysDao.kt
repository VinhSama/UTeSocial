package com.utesocial.android.feature_post.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_post.domain.model.PostsByUserIdRemoteKeys

@Dao
interface PostsByUserIdRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<PostsByUserIdRemoteKeys>)
    @Query("Select * from PostsByUserIdRemoteKeys where postId = :postId and userId = :authorId")
    suspend fun getRemoteKeys(postId: String, authorId: String) : PostsByUserIdRemoteKeys
    @Query("Delete from PostsByUserIdRemoteKeys")
    suspend fun clearAll()
}