package com.utesocial.android.feature_search.data.data_source.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.network.dto.SearchDto
import com.utesocial.android.feature_search.domain.model.SearchUser
import com.utesocial.android.feature_search.domain.use_case.SearchUseCase
import com.utesocial.android.remote.networkState.NetworkState
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchPageKeyedDataSource(
    private val searchUseCase: SearchUseCase,
    private val search: String,
    private val disposable: CompositeDisposable
) : PagingSource<Int, SearchUser>() {

    companion object {

        private const val STARTING_PAGE_INDEX = 1
    }

    val responseState = MutableLiveData<NetworkState>()

    override fun getRefreshKey(state: PagingState<Int, SearchUser>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUser> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return suspendCoroutine { continuation ->
            Debug.log("SearchPageKeyed", "load")
            searchUseCase(search, position, params.loadSize).process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<SearchDto>?> {

                    override fun onChanged(response: SimpleResponse<AppResponse<SearchDto>?>) {
                        Debug.log("SearchPageKeyed", "load:onChanged:$response")
                        responseState.postValue(response.getNetworkState())

                        if (response.isSuccessful()) {
                            response.getResponseBody()?.data?.let {
                                continuation.resume(
                                    LoadResult.Page(
                                        data = it.users,
                                        prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                                        nextKey = if (it.users.isEmpty()) null else position + 1
                                    )
                                )
                            }
                        }

                        if (response.isFailure()) {
                            continuation.resume(LoadResult.Error(Exception(response.getError()?.undefinedMessage)))
                        }
                    }
                }
            )
        }
    }
}