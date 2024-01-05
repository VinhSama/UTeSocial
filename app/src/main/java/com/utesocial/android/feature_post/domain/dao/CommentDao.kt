package com.utesocial.android.feature_post.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_post.domain.model.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<Comment>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(comment: Comment)
    @Query("Select * from Comment where post = :postId")
    fun getCommentsByPostId(postId: String) : PagingSource<Int, Comment>
    @Query("Delete from Comment")
    suspend fun clearAll()
}