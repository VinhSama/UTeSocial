package com.utesocial.android.feature_profile.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_profile.data.data_source.paging.MyPostPageKeyedDataSource
import com.utesocial.android.feature_profile.domain.model.UsernameReq
import com.utesocial.android.feature_profile.domain.use_case.ProfileUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val preferenceManager: PreferenceManager,
    private val authorizedUserObservable: BehaviorSubject<User>,
    private val disposable: CompositeDisposable
) : ViewModel() {

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
                            authorizedUserObservable.onNext(it)
                        }
                        mutableLiveData.postValue(response)
                    } else {
                        mutableLiveData.postValue(response)
                    }
                }
            })
        return mutableLiveData
    }

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

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}