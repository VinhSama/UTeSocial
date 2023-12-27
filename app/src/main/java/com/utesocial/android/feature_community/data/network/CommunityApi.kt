package com.utesocial.android.feature_community.data.network

import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import com.utesocial.android.feature_community.data.network.dto.FriendsListResponse
import com.utesocial.android.feature_community.data.network.dto.SearchUserResponse
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityApi {

    @GET("")
    suspend fun getCommunityInfo(): List<CommunityDto>
    @GET("friends")
    fun getFriendsList(@Query("page") page: Int, @Query("limit") limit: Int, @Query("search") search: String) : SimpleCall<AppResponse<FriendsListResponse>>
    @GET("search/users")
    fun searchUsers(@Query("page") page: Int, @Query("limit") limit: Int, @Query("search") search: String) : SimpleCall<AppResponse<SearchUserResponse>>
}