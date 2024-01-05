package com.utesocial.android.feature_post.presentation.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.room.withTransaction
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.datasource.database.PostDatabase
import com.utesocial.android.feature_post.data.network.dto.SendCommentResponse
import com.utesocial.android.feature_post.data.network.request.SendCommentRequest
import com.utesocial.android.feature_post.data.repository.CommentsInPostRemoteMediator
import com.utesocial.android.feature_post.domain.model.Comment
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val database: PostDatabase,
    private val authorizedUser: BehaviorSubject<User>
) : ViewModel() {
    val disposable = CompositeDisposable()
    val inputSelectorUsing = MutableLiveData<InputSelectorEvent>(InputSelectorEvent.NoneSelector)
    val commentQueue: PublishProcessor<Pair<String, SendCommentRequest>> = PublishProcessor.create()
    val onCommentResponseState: MutableLiveData<SimpleResponse<AppResponse<SendCommentResponse>?>> =
        MutableLiveData()
    val validateUIState = MutableLiveData(false)

    init {
        disposable.add(
            commentQueue
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    sendComment(it)
                }
        )
    }

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

    private fun sendComment(payload: Pair<String, SendCommentRequest>) {
        authorizedUser.value.takeIf { it != User.EMPTY && it != null }
            ?.apply {
                val latest = Date()
                val newComment = Comment(
                    latest.time.toString(),
                    payload.second.text,
                    createdAt = latest,
                    fullName = "$firstName $lastName",
                    username = username,
                    avatar = avatar?.url ?: "",
                    post = payload.first
                )
                postUseCase.sendCommentUseCase
                    .invoke(payload.first, payload.second)
                    .process(
                        disposable,
                        onStateChanged = object :
                            SimpleCall.OnStateChanged<AppResponse<SendCommentResponse>?> {
                            override fun onChanged(response: SimpleResponse<AppResponse<SendCommentResponse>?>) {
                                if (response.isRunning()) {
                                    viewModelScope.launch {
                                        database.getCommentDao().insertOne(newComment)
                                    }
                                }
                                if (response.isSuccessful()) {
                                    response.getResponseBody()?.data?.comment?.let {
                                        viewModelScope.launch {
                                            database.withTransaction {
                                                database.getCommentDao().deleteOne(newComment)
                                                database.getCommentDao().insertOne(it)
                                            }
                                        }
                                    }
                                }
                                if (response.isFailure()) {
                                    viewModelScope.launch {
                                        database.getCommentDao().deleteOne(newComment)
                                    }
                                    onCommentResponseState.postValue(response)
                                }
                            }

                        }
                    )
            }

    }

    override fun onCleared() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}