package com.utesocial.android.feature_search.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.utesocial.android.core.domain.use_case.MainUseCase
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.data_source.database.SearchCacheDatabase
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.feature_search.domain.model.SearchRemoteKeys
import com.utesocial.android.feature_search.domain.model.SearchUser
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
class SearchUsersRemoteMediator(
    private val searchCacheDatabase: SearchCacheDatabase,
    private val mainUseCase: MainUseCase,
    private val disposable: CompositeDisposable,
    private val coroutineScope: CoroutineScope,
    private val search: String
) : RemoteMediator<Int, SearchUser>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchUser>
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
            mainUseCase.searchUseCase
                .invoke(search, page, state.config.pageSize)
                .process(
                    disposable,
                    onStateChanged = object :
                        SimpleCall.OnStateChanged<AppResponse<SearchDto>?> {
                        override fun onChanged(response: SimpleResponse<AppResponse<SearchDto>?>) {
                            if (response.isSuccessful()) {
                                var endOfList: Boolean? = null
                                response.getResponseBody()?.data?.users?.apply {
                                    endOfList = isEmpty() || (size / state.config.pageSize < 1)
                                }
                                this@SearchUsersRemoteMediator.coroutineScope.launch {
                                    searchCacheDatabase.withTransaction {
                                        if (loadType == LoadType.REFRESH) {
                                            searchCacheDatabase.getSearchRemoteKeysDao().clearAll()
                                            searchCacheDatabase.getSearchDao().clearAll()
                                        }
                                        val prevKey =
                                            if (page == STARTING_PAGE_INDEX) null else page - 1
                                        val nextKey = if (endOfList == true) null else page + 1
                                        val keys =
                                            response.getResponseBody()?.data?.users?.map { user ->
                                                SearchRemoteKeys(user.userId, prevKey, nextKey)
                                            }
                                        keys?.let {
                                            searchCacheDatabase.getSearchRemoteKeysDao()
                                                .insertRemote(it)
                                        }
                                        response.getResponseBody()?.data?.users?.let {
                                            searchCacheDatabase.getSearchDao().insertAll(it)
                                        }
                                    }
                                    endOfList?.let {
                                        continuation.resume(MediatorResult.Success(it))
                                    }
                                }
                            }
                            if (response.isFailure()) {
                                continuation.resume(MediatorResult.Error(ResponseException(response.getError())))
                            }
                        }

                    }
                )
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, SearchUser>
    ): Any {
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

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, SearchUser>): SearchRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.userId?.let { repId ->
                    searchCacheDatabase.getSearchRemoteKeysDao().getRemoteKeys(repId)
                }
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, SearchUser>): SearchRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { search ->
                    searchCacheDatabase.getSearchRemoteKeysDao().getRemoteKeys(search.userId)
                }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, SearchUser>): SearchRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { search ->
                    searchCacheDatabase.getSearchRemoteKeysDao().getRemoteKeys(search.userId)
                }
        }
    }
}