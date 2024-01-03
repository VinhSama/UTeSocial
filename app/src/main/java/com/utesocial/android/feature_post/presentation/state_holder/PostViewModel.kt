package com.utesocial.android.feature_post.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PrivacyResponse
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    fun changePrivacy(
        postId: String,
        changePrivacyRequest: PrivacyRequest
    ): LiveData<SimpleResponse<AppResponse<PrivacyResponse>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<PrivacyResponse>?>> = MutableLiveData()
        postUseCase.changePrivacyUseCase(postId, changePrivacyRequest).process(
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
        postUseCase.deletePostUseCase(postId).process(
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