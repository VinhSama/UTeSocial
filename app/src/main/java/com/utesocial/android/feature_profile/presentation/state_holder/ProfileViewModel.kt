package com.utesocial.android.feature_profile.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.datasource.database.PostDatabase
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.data.repository.PostsByUserIdRemoteMediator
import com.utesocial.android.feature_post.domain.model.LikesPostHeader
import com.utesocial.android.feature_post.domain.model.PostInteraction
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.feature_profile.data.data_source.paging.MyPostPageKeyedDataSource
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.feature_profile.domain.use_case.ProfileUseCase
import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val database: PostDatabase,
    private val postUseCase: PostUseCase,
    private val profileUseCase: ProfileUseCase,
    private val preferenceManager: PreferenceManager,
    val authorizedUser: BehaviorSubject<User>,
    private val disposable: CompositeDisposable
) : ViewModel() {
    val likeStateInProcessing = HashSet<String>()
    val onLikeStateChanged: PublishProcessor<PostInteraction> = PublishProcessor.create()
    val onErrorResponseState: MutableLiveData<Error?> = MutableLiveData()


    init {
        disposable
            .add(
                onLikeStateChanged
                    .groupBy { it.postModel.id }
                    .flatMap { group ->
                        group.debounce(300, TimeUnit.MILLISECONDS)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe { interaction ->
                        when (interaction.javaClass) {
                            PostInteraction.Like::class.java -> likePost(interaction.postModel)
                            PostInteraction.Unlike::class.java -> unlikePost(interaction.postModel)
                        }
                    }
            )
    }

    private fun List<LikesPostHeader>.updateLikes(
        currentUserId: String,
        isLiked: Boolean,
        fullName: String = ""
    ): List<LikesPostHeader> {
        val updatedLikes = toMutableList()
        val existingLike = updatedLikes.find { it.userId == currentUserId && !it.isFriend }
        if (existingLike != null) {
            updatedLikes.remove(existingLike)
        }
        if (isLiked) {
            val newLike =
                LikesPostHeader(userId = currentUserId, isFriend = false, fullName = fullName)
            updatedLikes.add(0, newLike)
        }
        return updatedLikes
    }

    private fun likePost(postModel: PostModel) {
        likeStateInProcessing.add(postModel.id)
        val likes = authorizedUser.value?.let {
            postModel.likes.updateLikes(
                it.userId,
                isLiked = true,
                it.firstName + " " + it.lastName
            )
        }

        var postModelUpdate =
            likes?.let { postModel.copy(likes = it, likeCounts = postModel.likeCounts + 1) }
        postUseCase.likePostUseCase
            .invoke(postModel.id)
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Int>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<Int>?>) {
                        if (response.isRunning()) {
                            viewModelScope.launch {
                                database.getPostDao()
                                    .update(postModelUpdate!!)
                            }
                        }
                        if (response.isSuccessful()) {
                            likeStateInProcessing.remove(postModel.id)
                            response.getResponseBody()?.data?.let {
                                postModelUpdate = postModel.copy(likes = likes!!, likeCounts = it)
                                viewModelScope.launch {
                                    database.getPostDao()
                                        .update(postModelUpdate!!)
                                }
                            }
                        }
                        if (response.isFailure()) {
                            likeStateInProcessing.remove(postModel.id)
                            viewModelScope.launch {
                                database.getPostDao()
                                    .update(postModel)
                            }
                            onErrorResponseState.postValue(response.getError())
                        }
                    }

                }
            )
    }

    private fun unlikePost(postModel: PostModel) {
        likeStateInProcessing.add(postModel.id)
        val likes = authorizedUser.value?.let {
            postModel.likes.updateLikes(
                it.userId,
                isLiked = false
            )
        }
        var postModelUpdate =
            likes?.let { postModel.copy(likes = it, likeCounts = postModel.likeCounts - 1) }
        postUseCase.unlikePostUseCase
            .invoke(postModel.id)
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Int>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<Int>?>) {
                        if (response.isRunning()) {
                            viewModelScope.launch {
                                database.getPostDao()
                                    .update(postModelUpdate!!)
                            }
                        }
                        if (response.isSuccessful()) {
                            likeStateInProcessing.remove(postModel.id)
                            response.getResponseBody()?.data?.let {
                                postModelUpdate = postModel.copy(likes = likes!!, likeCounts = it)
                                viewModelScope.launch {
                                    database.getPostDao()
                                        .update(postModelUpdate!!)
                                }
                            }
                        }
                        if (response.isFailure()) {
                            likeStateInProcessing.remove(postModel.id)
                            viewModelScope.launch {
                                database.getPostDao()
                                    .update(postModel)
                            }
                            onErrorResponseState.postValue(response.getError())
                        }

                    }

                }
            )

    }

    fun updateUsername(username: String): LiveData<SimpleResponse<AppResponse<User>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<User>?>> =
            MutableLiveData()
        profileUseCase.updateUsernameUseCase(UsernameReq(username = username)).process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<User>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<User>?>) {
                    if (response.isSuccessful()) {
                        response.getResponseBody()?.data?.let {
                            Debug.log("updateUsernameSuccess", it.toString())
                            preferenceManager.putObject(Constants.CURRENT_USER, it)
                            authorizedUser.onNext(it)
                        }
                        mutableLiveData.postValue(response)
                    } else {
                        mutableLiveData.postValue(response)
                    }
                }
            })
        return mutableLiveData
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPostsByUserId(userId: String) =
        Pager(
            config = PagingConfig(
                10
            ),
            pagingSourceFactory = {
                database.getPostDao().getPostsByUserId(userId)
            },
            remoteMediator = PostsByUserIdRemoteMediator(
                database,
                profileUseCase,
                disposable,
                viewModelScope,
                userId
            )
        ).liveData.cachedIn(viewModelScope)

    fun getMyPosts(
        userId: String,
        limit: Int
    ) = Pager(
        config = PagingConfig(
            pageSize = limit,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MyPostPageKeyedDataSource(profileUseCase, userId, disposable) }
    ).liveData.cachedIn(viewModelScope)

    fun changePrivacy(
        postId: String,
        changePrivacyRequest: PrivacyRequest
    ): LiveData<SimpleResponse<AppResponse<PrivacyResponse>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<PrivacyResponse>?>> = MutableLiveData()
        profileUseCase.changePrivacyUseCase(postId, changePrivacyRequest).process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<PrivacyResponse>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<PrivacyResponse>?>) {
                    if (response.isSuccessful()) {
                        response.getResponseBody()?.data?.let { Debug.log("changePrivacySuccess", it.toString()) }
                    }
                    mutableLiveData.postValue(response)
                }
            }
        )
        return mutableLiveData
    }

    fun deleteMyPost(postId: String): LiveData<SimpleResponse<AppResponse<Void>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Void>?>> = MutableLiveData()
        profileUseCase.deletePostUseCase(postId).process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Void>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<Void>?>) {
                    if (response.isSuccessful()) {
                        response.getResponseBody()?.data?.let { Debug.log("deletePostSuccess", it.toString()) }
                    }
                    mutableLiveData.postValue(response)
                }
            }
        )
        return mutableLiveData
    }

    fun changePrivacy(
        postModel: PostModel,
        changePrivacyRequest: PrivacyRequest
    ) {
        var updatePost = postModel.copy(privacyMode = changePrivacyRequest.privacyMode)
        postUseCase
            .changePrivacyUseCase(postModel.id, changePrivacyRequest)
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<PrivacyResponse>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<PrivacyResponse>?>) {
                        if (response.isRunning()) {
                            viewModelScope.launch {
                                database.getPostDao().update(updatePost)
                            }
                        }
                        if (response.isSuccessful()) {
                            response.getResponseBody()?.data?.let {
                                updatePost = updatePost.copy(
                                    updatedAt = it.updatedAt,
                                    privacyMode = it.privacyMode
                                )
                                viewModelScope.launch {
                                    database.getPostDao().update(updatePost)
                                }
                            }
                        }
                        if (response.isFailure()) {
                            viewModelScope.launch {
                                database.getPostDao().update(postModel)
                            }
                            onErrorResponseState.postValue(response.getError())
                        }
                    }
                }
            )
    }

    fun deleteMyPost(postModel: PostModel): LiveData<SimpleResponse<AppResponse<Void>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Void>?>> = MutableLiveData()
        postUseCase.deletePostUseCase(postModel.id).process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Void>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<Void>?>) {
                    if(response.isRunning()) {
                        viewModelScope.launch {
                            database.getPostDao()
                                .deleteOne(postModel)
                        }
                    }
                    if(response.isFailure()) {
                        viewModelScope.launch {
                            database.getPostDao()
                                .insertOne(postModel)
                        }
                    }
                    mutableLiveData.postValue(response)
                }
            }
        )
        return mutableLiveData
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}