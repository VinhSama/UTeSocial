package com.utesocial.android.feature_post.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.datasource.database.PostDatabase
import com.utesocial.android.feature_post.data.network.dto.CommentsResponse
import com.utesocial.android.feature_post.domain.model.Comment
import com.utesocial.android.feature_post.domain.model.CommentRemoteKeys
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
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
class CommentsInPostRemoteMediator(
    private val postDatabase: PostDatabase,
    private val postUseCase: PostUseCase,
    private val disposable: CompositeDisposable,
    private val coroutineScope: CoroutineScope,
    private val postId: String
) : RemoteMediator<Int, Comment>(){

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comment>
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
            postUseCase.getCommentsByPostIdUseCase
                .invoke(postId, page, state.config.pageSize)
                .process(
                    disposable,
                    onStateChanged = object :
                        SimpleCall.OnStateChanged<AppResponse<CommentsResponse>?> {
                        override fun onChanged(response: SimpleResponse<AppResponse<CommentsResponse>?>) {
                            if(response.isSuccessful()) {
                                var endOfList : Boolean? = null
                                response.getResponseBody()?.data?.comments?.apply {
                                    endOfList = isEmpty() || (size / state.config.pageSize < 1)
                                }
                                this@CommentsInPostRemoteMediator.coroutineScope.launch {
                                    postDatabase.withTransaction {
                                        if(loadType == LoadType.REFRESH) {
                                            postDatabase.getCommentRemoteKeysDao().clearAll()
                                            postDatabase.getCommentDao().clearAll()
                                        }
                                        val prevKey =
                                            if(page == STARTING_PAGE_INDEX) null else page - 1
                                        val nextKey = if(endOfList == true) null else page + 1
                                        val keys =
                                            response.getResponseBody()?.data?.comments?.map { comment ->
                                                CommentRemoteKeys(comment.commentId, prevKey, nextKey)
                                            }
                                        keys?.let {
                                            postDatabase.getCommentRemoteKeysDao().insertRemote(keys)
                                        }
                                        response.getResponseBody()?.data?.comments?.let {
                                            postDatabase.getCommentDao().insertAll(it)
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

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Comment>): Any {
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

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, Comment>): CommentRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.commentId?.let { repId ->
                    postDatabase.getCommentRemoteKeysDao().getRemoteKeys(repId)
                }
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Comment>): CommentRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { comment -> postDatabase.getCommentRemoteKeysDao().getRemoteKeys(comment.commentId) }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Comment>): CommentRemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { comment -> postDatabase.getCommentRemoteKeysDao().getRemoteKeys(comment.commentId) }
        }
    }

}