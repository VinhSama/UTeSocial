package com.utesocial.android.feature_community.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.feature_community.data.datasource.database.CommunityDatabase
import com.utesocial.android.feature_community.data.network.dto.FriendRequestsResponse
import com.utesocial.android.feature_community.domain.model.FriendRequest
import com.utesocial.android.feature_community.domain.model.FriendRequestRemoteKeys
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPagingApi::class)
class FriendRequestsRemoteMediator(
    private val communityDatabase: CommunityDatabase,
    private val communityUseCase: CommunityUseCase,
    private val disposable: CompositeDisposable,
    private val coroutineScope: CoroutineScope,
    private val search: String
) : RemoteMediator<Int, FriendRequest>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FriendRequest>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }
        return suspendCoroutine { continuation ->
            communityUseCase.getFriendRequestsUseCase
                .invoke(page, state.config.pageSize, search)
                .process(
                    disposable,
                    onStateChanged = object :
                        SimpleCall.OnStateChanged<AppResponse<FriendRequestsResponse>?> {
                        override fun onChanged(response: SimpleResponse<AppResponse<FriendRequestsResponse>?>) {
                            if(response.isSuccessful()) {
                                var endOfList : Boolean? = null
                                response.getResponseBody()?.data?.requests?.apply {
                                    endOfList = isEmpty() || (size / state.config.pageSize < 1)
                                }
                                this@FriendRequestsRemoteMediator.coroutineScope.launch {
                                    communityDatabase.withTransaction {
                                        if(loadType == LoadType.REFRESH) {
                                            communityDatabase.getRequestRemoteKeysDao().clearAll()
                                            communityDatabase.getFriendRequestsDao().clearAll()
                                        }
                                        val prevKey =
                                            if(page == STARTING_PAGE_INDEX) null else page - 1
                                        val nextKey = if (endOfList == true) null else page + 1
                                        val keys =
                                            response.getResponseBody()?.data?.requests?.map { request ->
                                                FriendRequestRemoteKeys(request.requestId, prevKey, nextKey)
                                            }
                                        keys?.let {
                                            communityDatabase.getRequestRemoteKeysDao().insertRemote(keys)
                                        }
                                        response.getResponseBody()?.data?.requests?.let {
                                            communityDatabase.getFriendRequestsDao().insertAll(it)
                                        }
                                    }
                                    endOfList?.let {
                                        continuation.resume(MediatorResult.Success(it))
                                    }
                                }
                            }
                            if(response.isFailure()) {
                                continuation.resume(MediatorResult.Error(ResponseException(response.getError())))
                            }
                        }

                    }
                )
        }
    }


    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, FriendRequest>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, FriendRequest>): FriendRequestRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.requestId?.let { repId ->
                    communityDatabase.getRequestRemoteKeysDao().getRemoteKeys(repId)
                }
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, FriendRequest>): FriendRequestRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { friendRequest -> communityDatabase.getRequestRemoteKeysDao().getRemoteKeys(friendRequest.requestId) }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, FriendRequest>): FriendRequestRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { friendRequest -> communityDatabase.getRequestRemoteKeysDao().getRemoteKeys(friendRequest.requestId) }
        }
    }
}