package com.utesocial.android.feature_community.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.data.datasource.database.CommunityDatabase
import com.utesocial.android.feature_community.data.network.request.AnswerFriendRequest
import com.utesocial.android.feature_community.domain.model.FriendRequest
import com.utesocial.android.feature_community.domain.repository.FriendRequestsRemoteMediator
import com.utesocial.android.feature_community.domain.repository.FriendsListRemoteMediator
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.presentation.state_holder.state.CommunityState
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityUseCase: CommunityUseCase,
    private val database: CommunityDatabase
) : ViewModel() {
    val disposable = CompositeDisposable()
    val searchFriendsList = BehaviorSubject.createDefault("")
    val searchFriendRequest = BehaviorSubject.createDefault("")
    val listenFriendCountChanged = BehaviorSubject.createDefault(0)

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
                searchFriendRequest.value?.trim() ?: ""
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
                searchFriendsList.value?.trim() ?: "",
                listenFriendCountChanged
            )
        ).liveData.cachedIn(viewModelScope)

    fun onAcceptRequest(friendRequest: FriendRequest) : LiveData<SimpleResponse<AppResponse<Int>?>> {
        val responseState = MutableLiveData<SimpleResponse<AppResponse<Int>?>>()
        var updateRequest = friendRequest.copy(status = FriendRequest.FriendState.ACCEPTED)

        communityUseCase.answerFriendRequestUseCase
            .invoke(AnswerFriendRequest(updateRequest.status, updateRequest.requestId))
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Int>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<Int>?>) {
                        if(response.isRunning()) {
                            viewModelScope.launch {
                                database.getFriendRequestsDao()
                                    .updateRequestAfterResponse(updateRequest)
                            }
                        }
                        if(response.isSuccessful()) {
                            viewModelScope.launch {
                                database.getFriendsListDao()
                                    .insertOne(updateRequest.sender)
                            }
                            response.getResponseBody()?.data.let {
                                listenFriendCountChanged.onNext(it)
                            }
                            Debug.log("onAcceptRequest", "successful request")
                        }
                        if(response.isFailure()) {
                            updateRequest = updateRequest.copy(status = FriendRequest.FriendState.PENDING)
                            viewModelScope.launch {
                                database.getFriendRequestsDao()
                                    .updateRequestAfterResponse(updateRequest)
                            }
                        }
                        responseState.postValue(response)

                    }

                }
            )
        return responseState
    }

    fun onDenyRequest(friendRequest: FriendRequest) : LiveData<SimpleResponse<AppResponse<Int>?>> {
        val responseState = MutableLiveData<SimpleResponse<AppResponse<Int>?>>()
        var updateRequest = friendRequest.copy(status = FriendRequest.FriendState.REJECTED)
        communityUseCase.answerFriendRequestUseCase
            .invoke(AnswerFriendRequest(updateRequest.status, updateRequest.requestId))
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Int>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<Int>?>) {
                        if(response.isRunning()) {
                            viewModelScope.launch {
                                database.getFriendRequestsDao()
                                    .updateRequestAfterResponse(updateRequest)
                            }
                            if(response.isFailure()) {
                                updateRequest = updateRequest.copy(status = FriendRequest.FriendState.PENDING)
                                viewModelScope.launch {
                                    database.getFriendRequestsDao()
                                        .updateRequestAfterResponse(updateRequest)
                                }
                            }
                            responseState.postValue(response)
                        }
                    }

                }
            )
        return responseState
    }

    fun onProfileClick(friendRequest: FriendRequest) {

    }
    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}