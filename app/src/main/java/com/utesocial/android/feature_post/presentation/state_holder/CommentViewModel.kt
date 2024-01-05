package com.utesocial.android.feature_post.presentation.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_post.data.datasource.database.PostDatabase
import com.utesocial.android.feature_post.data.repository.CommentsInPostRemoteMediator
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val database: PostDatabase
) : ViewModel() {
    val disposable = CompositeDisposable()
    val inputSelectorUsing = MutableLiveData<InputSelectorEvent>(InputSelectorEvent.NoneSelector)
    val validateUIState = MutableLiveData(false)

    @OptIn(ExperimentalPagingApi::class)
    fun getCommentsInPost(postId: String) =
        Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = {
                database.getCommentDao().getCommentsByPostId(postId)
            },
            remoteMediator = CommentsInPostRemoteMediator(
                database,
                postUseCase,
                disposable,
                viewModelScope,
                postId
            )
        ).liveData.cachedIn(viewModelScope)

    override fun onCleared() {
        if(!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}