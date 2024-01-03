package com.utesocial.android.feature_search.data.data_source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.utesocial.android.feature_search.domain.model.SearchUserRemoteKeys

@Dao
interface SearchUserRemoteKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertRemote(list: List<SearchUserRemoteKeys>)

    @Query("SELECT * FROM SearchUserRemoteKeys where userId = :id")
    suspend fun getRemoteKeys(id: String): SearchUserRemoteKeys

    @Query("DELETE FROM SearchUserRemoteKeys")
    suspend fun clearAll()
}