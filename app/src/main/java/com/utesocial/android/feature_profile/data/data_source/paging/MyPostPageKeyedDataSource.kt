package com.utesocial.android.feature_profile.data.data_source.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_profile.domain.use_case.ProfileUseCase
import com.utesocial.android.remote.networkState.NetworkState
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MyPostPageKeyedDataSource(
    private val profileUseCase: ProfileUseCase,
    private val userId: String,
    private val disposable: CompositeDisposable
) : PagingSource<Int, PostModel>() {

    companion object {

        private const val STARTING_PAGE_INDEX = 1
    }

    val responseState = MutableLiveData<NetworkState>()

    override fun getRefreshKey(state: PagingState<Int, PostModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostModel> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return suspendCoroutine { continuation ->
            Debug.log("PostPageKeyed", "load")
            profileUseCase.getMyPostsUseCase(userId, position, params.loadSize).process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<PostBody>?> {

                    override fun onChanged(response: SimpleResponse<AppResponse<PostBody>?>) {
                        Debug.log("PostPageKeyed", "load:onChanged:$response")
                        responseState.postValue(response.getNetworkState())

                        if (response.isSuccessful()) {
                            response.getResponseBody()?.data?.let {
                                continuation.resume(
                                    LoadResult.Page(
                                        data = it.posts,
                                        prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                                        nextKey = if (it.posts.isEmpty()) null else position + 1
                                    )
                                )
                            }
                        }

                        if (response.isFailure()) {
                            continuation.resume(LoadResult.Error(Exception(response.getError()?.undefinedMessage)))
                        }
                    }
                })
        }
    }
}