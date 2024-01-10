package com.utesocial.android.feature_search.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utesocial.android.feature_search.domain.model.SearchRemoteKeys

@Dao
interface SearchRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemote(list: List<SearchRemoteKeys>)

    @Query("Select * from  SearchRemoteKeys where userId = :id")
    suspend fun getRemoteKeys(id: String) : SearchRemoteKeys

    @Query("Delete from SearchRemoteKeys")
    suspend fun clearAll()
}