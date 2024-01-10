package com.utesocial.android.feature_post.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utesocial.android.feature_post.domain.model.PostModel

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(post: List<PostModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(postModel: PostModel)
    @Query("Select * from PostModel Order by createdAt desc")
    fun getFeedPosts() : PagingSource<Int, PostModel>
    @Query("Delete from PostModel")
    suspend fun clearAll()
    @Update
    suspend fun update(postModel: PostModel)
    @Delete
    suspend fun deleteOne(postModel: PostModel)
    @Query("Select * from PostModel where userAuthor_id = :userId Order by createdAt desc")
    fun getPostsByUserId(userId: String) : PagingSource<Int, PostModel>
}