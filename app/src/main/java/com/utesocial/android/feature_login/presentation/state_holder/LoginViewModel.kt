package com.utesocial.android.feature_login.presentation.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.util.ValidationResult
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_login.domain.use_case.ValidateEmail
import com.utesocial.android.feature_login.domain.use_case.ValidatePassword
import com.utesocial.android.feature_login.presentation.state_holder.state.LoginFormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val loginUseCase: LoginUseCase,
    private val preferenceManager: PreferenceManager
    ) : ViewModel() {

    val disposable : CompositeDisposable = CompositeDisposable()
    val emailInputSubject : BehaviorSubject<String> = BehaviorSubject.create()
    val passwordInputSubject : BehaviorSubject<String> = BehaviorSubject.create()
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

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }


}