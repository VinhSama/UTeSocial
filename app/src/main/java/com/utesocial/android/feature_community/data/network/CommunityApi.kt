package com.utesocial.android.feature_community.data.network

import com.utesocial.android.feature_community.data.network.dto.CommunityDto
import com.utesocial.android.feature_community.data.network.dto.FriendRequestsResponse
import com.utesocial.android.feature_community.data.network.dto.FriendsListResponse
import com.utesocial.android.feature_community.data.network.dto.SearchUserResponse
import com.utesocial.android.feature_community.data.network.request.AnswerFriendRequest
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface CommunityApi {

    @GET("")
    suspend fun getCommunityInfo(): List<CommunityDto>
    @GET("friends")
    fun getFriendsList(@Query("page") page: Int, @Query("limit") limit: Int, @Query("search") search: String) : SimpleCall<AppResponse<FriendsListResponse>>
    @GET("search/users")
    fun searchUsers(@Query("page") page: Int, @Query("limit") limit: Int, @Query("search") search: String) : SimpleCall<AppResponse<SearchUserResponse>>
    @GET("friends/requests")
    fun getFriendRequest(@Query("page") page: Int, @Query("limit") limit: Int, @Query("search") search: String) : SimpleCall<AppResponse<FriendRequestsResponse>>
    @PUT("friends")
    fun answerFriendRequest(@Body answerFriendRequest: AnswerFriendRequest) : SimpleCall<AppResponse<Int>>
}