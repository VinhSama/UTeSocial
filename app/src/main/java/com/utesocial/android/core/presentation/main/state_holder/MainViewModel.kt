package com.utesocial.android.core.presentation.main.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.room.withTransaction
import com.utesocial.android.UteSocial
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.domain.use_case.MainUseCase
import com.utesocial.android.feature_community.domain.repository.FriendRequestsRemoteMediator
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.feature_search.data.data_source.database.SearchUserDatabase
import com.utesocial.android.feature_search.domain.repository.SearchUserRemoteMediator
import com.utesocial.android.feature_search.domain.use_case.SearchUserUseCase
import com.utesocial.android.remote.simpleCallAdapter.SimpleCall
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    private val searchUserDatabase: SearchUserDatabase,
    private val authorizedUserObservable: BehaviorSubject<User>,
    private val preferenceManager: PreferenceManager,
    val unauthorizedEventBroadcast: MutableLiveData<Boolean>
) : ViewModel() {

    val authorizedUser = MutableLiveData<User>()
    val disposable = CompositeDisposable()

    val searchUserRequest = BehaviorSubject.createDefault("")

    init {
        disposable.add(authorizedUserObservable.distinctUntilChanged().subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io()).subscribe { authorizedUser.postValue(it) })
    }

    fun handleUnauthorizedEvent() {
        preferenceManager.remove(Constants.CURRENT_USER)
        preferenceManager.remove(Constants.ACCESS_TOKEN)
        preferenceManager.remove(Constants.REFRESH_TOKEN)

        authorizedUserObservable.onNext(User.EMPTY)
        unauthorizedEventBroadcast.postValue(false)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun searchUsers() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { searchUserDatabase.getSearchUserDao().getSearchUsers() },
        remoteMediator = SearchUserRemoteMediator(
            searchUserDatabase,
            mainUseCase.searchUserUseCase,
            disposable,
            viewModelScope,
            searchUserRequest.value?.trim() ?: ""
        )
    ).liveData.cachedIn(viewModelScope)

    fun clearAllData() = viewModelScope.launch {
        searchUserDatabase.withTransaction {
            searchUserDatabase.getSearchUserRemoteKeysDao().clearAll()
            searchUserDatabase.getSearchUserDao().clearAll()
        }
    }

    fun sendFriendRequest(receiverId: String): LiveData<SimpleResponse<AppResponse<Void>?>> {
        val mutableLiveData: MutableLiveData<SimpleResponse<AppResponse<Void>?>> = MutableLiveData()
        mainUseCase.sendFriendRequestUseCase(receiverId).process(
            disposable,
            onStateChanged = object : SimpleCall.OnStateChanged<AppResponse<Void>?> {

                override fun onChanged(response: SimpleResponse<AppResponse<Void>?>) {
                    if (response.isSuccessful()) {
                        response.getResponseBody()?.data?.let { Debug.log("sendRequestSuccess", it.toString()) }
                    }
                    mutableLiveData.postValue(response)
                }
            }
        )
        return mutableLiveData
    }

    override fun onCleared() {
        if(disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}