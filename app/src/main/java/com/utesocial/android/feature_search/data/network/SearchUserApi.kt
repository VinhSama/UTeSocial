package com.utesocial.android.feature_search.data.network

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchUserDto
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchUserApi {

    @GET("search/users")
    fun searchUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String
    ): SimpleCall<AppResponse<SearchUserDto>>
}