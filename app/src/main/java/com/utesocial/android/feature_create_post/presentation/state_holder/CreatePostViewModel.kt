package com.utesocial.android.feature_create_post.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_post.data.network.dto.PostResourcesBody
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postUseCase: PostUseCase
): ViewModel() {

    val disposable = CompositeDisposable()
    val inputSelectorUsing = MutableLiveData<InputSelectorEvent>(InputSelectorEvent.NoneSelector)
    val mediaItems by lazy { MutableLiveData<List<MediaItem>>(emptyList())}
    val validateUIState = MutableLiveData(false)

    fun uploadPostResources(attachments: RequestBody) : LiveData<SimpleResponse<AppResponse<PostResourcesBody>?>> {
        val mutableLiveData = MutableLiveData<SimpleResponse<AppResponse<PostResourcesBody>?>>()
        postUseCase.uploadPostResourcesUseCase
            .invoke(attachments)
            .process(disposable, onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<PostResourcesBody>?> {
                override fun onChanged(response: SimpleResponse<AppResponse<PostResourcesBody>?>) {
                    if(response.isSuccessful()) {
                        response.getResponseBody()?.data?.let {
                            Debug.log("uploadPostResources", it.resources.toString())
                        }
                        mutableLiveData.postValue(response)
                    } else {
                        mutableLiveData.postValue(response)
                    }
                }

            })
        return mutableLiveData
    }

    fun createPost(createPostRequest: CreatePostRequest) : LiveData<SimpleResponse<AppResponse<PostModel>?>> {
        val mutableLiveData = MutableLiveData<SimpleResponse<AppResponse<PostModel>?>>()
        postUseCase.createPostUseCase
            .invoke(createPostRequest)
            .process(disposable, onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<PostModel>?> {
                override fun onChanged(response: SimpleResponse<AppResponse<PostModel>?>) {
                    if(response.isSuccessful()) {
                        response.getResponseBody()?.data.let {
                            Debug.log("createPost", it.toString())
                        }
                        mutableLiveData.postValue(response)
                    } else {
                        mutableLiveData.postValue(response)
                    }
                }

            })
        return mutableLiveData
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}