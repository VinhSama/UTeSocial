package com.utesocial.android.feature_search.data.data_source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.util.Converters
import com.utesocial.android.feature_search.data.data_source.database.dao.SearchUserDao
import com.utesocial.android.feature_search.data.data_source.database.dao.SearchUserRemoteKeysDao
import com.utesocial.android.feature_search.domain.model.SearchUser
import com.utesocial.android.feature_search.domain.model.SearchUserRemoteKeys

@Database(
    entities = [User::class, SearchUser::class, SearchUserRemoteKeys::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class SearchUserDatabase : RoomDatabase() {

    abstract fun getSearchUserDao(): SearchUserDao

    abstract fun getSearchUserRemoteKeysDao(): SearchUserRemoteKeysDao
}