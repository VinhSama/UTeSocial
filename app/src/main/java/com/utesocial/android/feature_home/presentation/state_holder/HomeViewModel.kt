package com.utesocial.android.feature_home.presentation.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.feature_home.presentation.state_holder.state.HomeState
import com.utesocial.android.feature_post.data.datasource.paging.PostPageKeyedDataSource
import com.utesocial.android.feature_post.domain.model.Like
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

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