package com.utesocial.android.feature_home.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_home.presentation.state_holder.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
                HomeViewModel(appModule.homeUseCase)
            }
        }
    }

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState

    init { getSuggestPostsUseCase() }

    fun getSuggestPostsUseCase() = homeUseCase.getSuggestPostsUseCase().onEach { resource ->
        when (resource) {
            is Resource.Loading -> _homeState.value = HomeState(isLoading = true)
            is Resource.Success -> _homeState.value = HomeState(posts = resource.data ?: emptyList())
            is Resource.Error -> _homeState.value = HomeState(error = resource.message ?: "An unexpected error occurred")
        }
    }.launchIn(viewModelScope)
}