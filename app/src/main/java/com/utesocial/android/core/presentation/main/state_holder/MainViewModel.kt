package com.utesocial.android.core.presentation.main.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authorizedUserObservable: BehaviorSubject<User>, ) : ViewModel() {
    val authorizedUser = MutableLiveData<User>()
    private val disposable = CompositeDisposable()
    init {
        disposable.add(
            authorizedUserObservable
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    authorizedUser.postValue(it)
                }
        )
    }
//    companion object {
//
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val savedStateHandle = createSavedStateHandle()
//                val application = this[APPLICATION_KEY] as UteSocial
//                MainViewModel(savedStateHandle)
//            }
//        }
//    }

    override fun onCleared() {
        if(disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}