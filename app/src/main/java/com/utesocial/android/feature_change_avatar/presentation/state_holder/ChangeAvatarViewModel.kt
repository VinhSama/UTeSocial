package com.utesocial.android.feature_change_avatar.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.feature_change_avatar.domain.use_case.ChangeAvatarUseCase
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    private val changeAvatarUseCase: ChangeAvatarUseCase,
    private val preferenceManager: PreferenceManager,
    private val authorizedUserObservable: BehaviorSubject<User>
) : ViewModel() {

    val disposable = CompositeDisposable()

    fun uploadAvatar(image: RequestBody): LiveData<SimpleResponse<AppResponse<Avatar>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Avatar>?>> =
            MutableLiveData()
        changeAvatarUseCase.uploadAvatarUseCase(image)
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Avatar>?> {

                    override fun onChanged(response: SimpleResponse<AppResponse<Avatar>?>) {
                        if (response.isSuccessful()) {
                            response.getResponseBody()?.data?.let {
                                Debug.log("updateAvatarSuccess", it.toString())
                                val user = authorizedUserObservable.value?.copy(avatar = it)
                                preferenceManager.putObject(Constants.CURRENT_USER, user!!)
                                authorizedUserObservable.onNext(user)
                            }
                            mutableLiveData.postValue(response)
                        } else {
                            mutableLiveData.postValue(response)
                        }
                    }
                }
            )
        return mutableLiveData
    }

    fun deleteAvatar(): LiveData<SimpleResponse<AppResponse<Void>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Void>?>> = MutableLiveData()
        changeAvatarUseCase.deleteAvatarUseCase().process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Void>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<Void>?>) {
                    if (response.isSuccessful()) {
                        response.getResponseBody()?.data?.let {
                            Debug.log("deleteAvatarSuccess", it.toString())
                            val user = authorizedUserObservable.value?.copy(avatar = null)
                            preferenceManager.putObject(Constants.CURRENT_USER, user!!)
                            authorizedUserObservable.onNext(user)
                        }
                        mutableLiveData.postValue(response)
                    } else {
                        mutableLiveData.postValue(response)
                    }
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