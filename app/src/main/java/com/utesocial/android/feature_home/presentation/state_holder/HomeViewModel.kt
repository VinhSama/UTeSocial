package com.utesocial.android.feature_home.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.UteSocial
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_home.domain.use_case.HomeUseCase
import com.utesocial.android.feature_home.presentation.state_holder.state.HomeState
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.datasource.paging.PostPageKeyedDataSource
import com.utesocial.android.feature_post.data.network.dto.PostBody
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {
//
//    companion object {
//
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
//                HomeViewModel(appModule.homeUseCase)
//            }
//        }
//    }
//
//    private val _homeState = MutableStateFlow(HomeState())
//    val homeState: StateFlow<HomeState> = _homeState
//
//    init { getSuggestPostsUseCase() }
//
//    fun getSuggestPostsUseCase() = homeUseCase.getSuggestPostsUseCase().onEach { resource ->
//        when (resource) {
//            is Resource.Loading -> _homeState.value = HomeState(isLoading = true)
//            is Resource.Success -> _homeState.value = HomeState(posts = resource.data ?: emptyList())
//            is Resource.Error -> _homeState.value = HomeState(error = resource.message ?: "An unexpected error occurred")
//        }
//    }.launchIn(viewModelScope)
//}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val disposable: CompositeDisposable) : ViewModel() {


    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState
    companion object {
        private const val PAGE_SIZE = 20
    }
//    init { getSuggestPostsUseCase() }


    fun getFeedPosts(limit : Int, ) = Pager(
        config = PagingConfig(
            pageSize = limit,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostPageKeyedDataSource(postUseCase, Like.UserType.USER, disposable) }
    ).liveData.cachedIn(viewModelScope)
}