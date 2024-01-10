package com.utesocial.android.feature_search.domain.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utesocial.android.feature_search.domain.model.SearchUser

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searchResult: List<SearchUser>)
    @Update
    suspend fun updateOne(searchUser: SearchUser)
    @Query("Select * from SearchUser")
    fun getSearchResults(): PagingSource<Int, SearchUser>
    @Query("Delete from SearchUser")
    suspend fun clearAll()
}