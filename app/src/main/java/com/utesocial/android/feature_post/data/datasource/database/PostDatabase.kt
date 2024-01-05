package com.utesocial.android.feature_post.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utesocial.android.core.presentation.util.Converters
import com.utesocial.android.feature_post.domain.dao.CommentDao
import com.utesocial.android.feature_post.domain.dao.CommentRemoteKeysDao
import com.utesocial.android.feature_post.domain.dao.PostDao
import com.utesocial.android.feature_post.domain.dao.PostRemoteKeysDao
import com.utesocial.android.feature_post.domain.model.Comment
import com.utesocial.android.feature_post.domain.model.CommentRemoteKeys
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostRemoteKeys

@Database(
    entities = [PostModel::class, PostRemoteKeys::class, Comment::class, CommentRemoteKeys::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun getPostDao() : PostDao
    abstract fun getPostRemoteKeysDao() : PostRemoteKeysDao
    abstract fun getCommentDao() : CommentDao
    abstract fun getCommentRemoteKeysDao() : CommentRemoteKeysDao
}