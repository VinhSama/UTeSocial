package com.utesocial.android.feature_community.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.data.datasource.database.CommunityDatabase
import com.utesocial.android.feature_community.domain.repository.FriendRequestsRemoteMediator
import com.utesocial.android.feature_community.domain.repository.FriendsListRemoteMediator
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.presentation.state_holder.state.CommunityState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityUseCase: CommunityUseCase,
    private val database: CommunityDatabase
) : ViewModel() {
    val disposable = CompositeDisposable()
    val searchFriendsList = BehaviorSubject.createDefault("")
    val searchFriendRequest = BehaviorSubject.createDefault("")

    @OptIn(ExperimentalPagingApi::class)
    fun getFriendRequests() =
        Pager(
            config = PagingConfig(
                10,
            ),
            pagingSourceFactory = {
                database.getFriendRequestsDao().getRequestsList()
            },
            remoteMediator = FriendRequestsRemoteMediator(
                database,
                communityUseCase,
                disposable,
                viewModelScope,
                searchFriendRequest.value ?: ""
            )
        ).liveData.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    fun getFriendsList() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                database.getFriendsListDao().getFriendsList()
            },
            remoteMediator = FriendsListRemoteMediator(
                database,
                communityUseCase,
                disposable,
                viewModelScope,
                searchFriendsList.value ?: ""
            )
        ).liveData.cachedIn(viewModelScope)

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}