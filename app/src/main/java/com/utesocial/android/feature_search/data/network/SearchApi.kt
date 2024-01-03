package com.utesocial.android.feature_search.data.network

import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/users")
    fun searchUsers(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): SimpleCall<AppResponse<SearchDto>>
}