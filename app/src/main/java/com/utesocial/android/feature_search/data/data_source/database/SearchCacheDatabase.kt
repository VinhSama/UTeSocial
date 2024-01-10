package com.utesocial.android.feature_search.data.data_source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utesocial.android.core.presentation.util.Converters
import com.utesocial.android.feature_search.domain.dao.SearchDao
import com.utesocial.android.feature_search.domain.dao.SearchRemoteKeysDao
import com.utesocial.android.feature_search.domain.model.SearchRemoteKeys
import com.utesocial.android.feature_search.domain.model.SearchUser

@Database(
    entities = [SearchUser::class, SearchRemoteKeys::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SearchCacheDatabase : RoomDatabase() {
    abstract fun getSearchDao() : SearchDao
    abstract fun getSearchRemoteKeysDao() : SearchRemoteKeysDao
}