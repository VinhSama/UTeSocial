package com.utesocial.android.feature_search.data.data_source.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.utesocial.android.feature_search.domain.model.SearchUser

@Dao
interface SearchUserDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(searchUsers: List<SearchUser>)

    @Query("SELECT * FROM SearchUser")
    fun getSearchUsers(): PagingSource<Int, SearchUser>

    @Query("DELETE FROM SearchUser")
    suspend fun clearAll()

    @Update
    suspend fun updateInfoAfterResponse(searchUser: SearchUser)
}