package com.utesocial.android.feature_community.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.data.datasource.database.FriendsListDatabase
import com.utesocial.android.feature_community.domain.repository.FriendsListRemoteMediator
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.presentation.state_holder.state.CommunityState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityUseCase: CommunityUseCase,
    private val database: FriendsListDatabase
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val _communityState = MutableStateFlow(CommunityState())
    val communityState: StateFlow<CommunityState> = _communityState

    init { getCommunityInfo() }

    fun getCommunityInfo() = communityUseCase.getCommunityInfoUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> _communityState.value = CommunityState(isLoading = true)
            is Resource.Success -> {
                val groups = resource.data?.groups ?: emptyList()
                val posts = resource.data?.posts ?: emptyList()
                _communityState.value = CommunityState(groups = groups, posts = posts)
            }
            is Resource.Error -> _communityState.value = CommunityState(error = resource.message ?: "An unexpected error occurred")
        }
    }.launchIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    fun getFriendsList() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                database.getFriendsListDao().getFriendsList()
            },
            remoteMediator = FriendsListRemoteMediator(
                database,
                communityUseCase,
                disposable,
                viewModelScope
            )
        ).liveData.cachedIn(viewModelScope)

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}