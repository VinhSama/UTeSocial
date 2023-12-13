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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CommunityViewModel(private val communityUseCase: CommunityUseCase) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
                CommunityViewModel(appModule.communityUseCase)
            }
        }
    }

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
}