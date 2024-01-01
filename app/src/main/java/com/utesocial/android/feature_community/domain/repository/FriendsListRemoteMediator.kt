package com.utesocial.android.feature_community.domain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.feature_community.data.datasource.database.CommunityDatabase
import com.utesocial.android.feature_community.data.network.dto.FriendsListResponse
import com.utesocial.android.feature_community.domain.model.FriendsListRemoteKeys
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.networkState.NetworkState
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPagingApi::class)
class FriendsListRemoteMediator(
    private val communityDatabase: CommunityDatabase,
    private val communityUseCase: CommunityUseCase,
    private val disposable: CompositeDisposable,
    private val coroutineScope: CoroutineScope,
    private val search: String,
    private val listenFriendCountChanged: BehaviorSubject<Int>
    ) : RemoteMediator<Int, User>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    val responseState = MutableLiveData<NetworkState>()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }
        return suspendCoroutine { continuation ->
            communityUseCase.getFriendsListUseCase
                .invoke(page, state.config.pageSize, search)
                .process(
                    disposable,
                    onStateChanged = object :
                        SimpleCall.OnStateChanged<AppResponse<FriendsListResponse>?> {
                        override fun onChanged(response: SimpleResponse<AppResponse<FriendsListResponse>?>) {
                            responseState.postValue(response.getNetworkState())
                            if (response.isSuccessful()) {
                                var endOfList : Boolean? = null
                                response.getResponseBody()?.data?.let { body ->
                                    body.friends.apply {
                                        endOfList = isEmpty() || (size / state.config.pageSize < 1)
                                    }
                                    if(search.trim().isEmpty()) {
                                        listenFriendCountChanged.onNext(body.totalCount)
                                    }
                                }

                                this@FriendsListRemoteMediator.coroutineScope.launch {
                                    communityDatabase.withTransaction {
                                        if (loadType == LoadType.REFRESH) {
                                            communityDatabase.getRemoteKeysDao().clearAll()
                                            communityDatabase.getFriendsListDao().clearAll()
                                        }
                                        val prevKey =
                                            if (page == STARTING_PAGE_INDEX) null else page - 1
                                        val nextKey = if (endOfList == true) null else page + 1
                                        val keys =
                                            response.getResponseBody()?.data?.friends?.map { user ->
                                                FriendsListRemoteKeys(user.userId, prevKey, nextKey)
                                            }
                                        keys?.let {
                                            communityDatabase.getRemoteKeysDao().insertRemote(it)
                                        }
                                        response.getResponseBody()?.data?.friends?.let {
                                            communityDatabase.getFriendsListDao().insertAll(it)
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

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, User>): Any {
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

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, User>): FriendsListRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.userId?.let { repId ->
                    communityDatabase.getRemoteKeysDao().getRemoteKeys(repId)
                }
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, User>): FriendsListRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { user -> communityDatabase.getRemoteKeysDao().getRemoteKeys(user.userId) }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, User>): FriendsListRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { user -> communityDatabase.getRemoteKeysDao().getRemoteKeys(user.userId) }
        }
    }


}