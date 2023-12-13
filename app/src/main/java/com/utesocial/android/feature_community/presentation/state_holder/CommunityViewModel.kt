package com.utesocial.android.feature_community.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_community.presentation.state_holder.state.CommunityState
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_home.presentation.state_holder.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CommunityViewModel(
    private val communityUseCase: CommunityUseCase,
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
                CommunityViewModel(appModule.communityUseCase, appModule.homeUseCase)
            }
        }
    }

    private val _communityState = MutableStateFlow(CommunityState())
    val communityState: StateFlow<CommunityState> = _communityState

    init {
        getJoinGroups()
        getSuggestPostsUseCase()
    }

    fun getJoinGroups() = communityUseCase.getJoinGroupsUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> _communityState.value.isLoading = true
            is Resource.Success -> _communityState.value.joinGroups = resource.data ?: emptyList()
            is Resource.Error -> _communityState.value.error = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)

    fun getSuggestPostsUseCase() = homeUseCase.getSuggestPostsUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> _communityState.value.isLoading = true
            is Resource.Success -> _communityState.value.postsGroup = resource.data ?: emptyList()
            is Resource.Error -> _communityState.value.error = resource.message ?: "An unexpected error occurred"
        }
    }.launchIn(viewModelScope)
}