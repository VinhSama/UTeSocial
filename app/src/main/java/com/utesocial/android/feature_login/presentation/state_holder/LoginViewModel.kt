package com.utesocial.android.feature_login.presentation.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.domain.util.ValidationResult
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_login.data.network.dto.LoginBody
import com.utesocial.android.feature_login.data.network.request.LoginRequest
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_login.domain.use_case.ValidateEmail
import com.utesocial.android.feature_login.domain.use_case.ValidatePassword
import com.utesocial.android.feature_login.presentation.state_holder.state.LoginFormEvent
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val loginUseCase: LoginUseCase,
    private val preferenceManager: PreferenceManager,
    private val authorizedUserObservable: BehaviorSubject<User>
    ) : ViewModel() {

    val disposable : CompositeDisposable = CompositeDisposable()
    val emailInputSubject : BehaviorSubject<String> = BehaviorSubject.createDefault("")
    val passwordInputSubject : BehaviorSubject<String> = BehaviorSubject.createDefault("")
    val validationDeliver : BehaviorSubject<ValidationResult> = BehaviorSubject.create()
    val submitUIState = MutableLiveData(false)

    init {
        disposable.add(
            BehaviorSubject.combineLatest(
                emailInputSubject,
                passwordInputSubject
            ) { i1, i2 ->
                ValidateEmail.validate(i1).errorType == null && ValidatePassword.validate(i2).errorType == null
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { submitUIState.postValue(it)}
        )
    }
    fun onEvent(loginFormEvent: LoginFormEvent) {
        when(loginFormEvent) {
            is LoginFormEvent.EmailChanged -> validationDeliver.onNext(ValidateEmail.validate(loginFormEvent.email))
            is LoginFormEvent.PasswordChanged ->validationDeliver.onNext(ValidatePassword.validate(loginFormEvent.password))
            else -> {

            }
        }
    }
    fun storeAndSetupUIState(loginBody: LoginBody) {
        preferenceManager.putString(Constants.ACCESS_TOKEN, loginBody.tokens.accessToken)
        preferenceManager.putString(Constants.REFRESH_TOKEN, loginBody.tokens.refreshToken)
        preferenceManager.putObject(Constants.CURRENT_USER, loginBody.user)
        authorizedUserObservable.onNext(loginBody.user)
    }
    fun login() : LiveData<SimpleResponse<AppResponse<LoginBody>?>> {
        val mutableLiveData : MutableLiveData<SimpleResponse<AppResponse<LoginBody>?>> = MutableLiveData<SimpleResponse<AppResponse<LoginBody>?>>()
        loginUseCase.getLoginUseCase
            .invoke(LoginRequest(emailInputSubject.value.toString(), passwordInputSubject.value.toString()))
            .process(disposable, onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<LoginBody>?> {
                override fun onChanged(response: SimpleResponse<AppResponse<LoginBody>?>) {
                    if(response.isSuccessful()) {
                        response.getResponseBody()?.data?.let { storeAndSetupUIState(it) }
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