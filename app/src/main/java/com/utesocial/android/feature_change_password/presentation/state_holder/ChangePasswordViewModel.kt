package com.utesocial.android.feature_change_password.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.feature_change_password.domain.model.ChangePasswordReq
import com.utesocial.android.feature_change_password.domain.use_case.ChangePasswordUseCase
import com.utesocial.android.feature_change_password.presentation.state_holder.state.ChangePasswordState
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private var _responseChangePassword = MutableStateFlow(ChangePasswordState())
    val responseChangePassword: StateFlow<ChangePasswordState> = _responseChangePassword
    val disposable = CompositeDisposable()

    //    fun changePassword(changePasswordReq: ChangePasswordReq)
//    = changePasswordUseCase(changePasswordReq).onEach { resource ->
//        when (resource) {
//            is Resource.Loading -> _responseChangePassword.value = ChangePasswordState()
//            is Resource.Success -> _responseChangePassword.value = ChangePasswordState(
//                isChanged = true, message = resource.data ?: "Password has been changed")
//            is Resource.Error -> _responseChangePassword.value = ChangePasswordState(
//                isChanged = false, message = resource.message ?: "An unexpected error occurred")
//        }
//    }.launchIn(viewModelScope)
    fun changePassword(changePasswordReq: ChangePasswordReq): LiveData<SimpleResponse<AppResponse<Void>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Void>?>> = MutableLiveData()
        changePasswordUseCase
            .invoke(changePasswordReq)
            .process(
                disposable,
                onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Void>?> {
                    override fun onChanged(response: SimpleResponse<AppResponse<Void>?>) {
                        Debug.log("changePassword", response.toString())
                        mutableLiveData.postValue(response)
                    }

                })
        return mutableLiveData
    }

    fun resetStatus() {
        _responseChangePassword.value = ChangePasswordState()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}