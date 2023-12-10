package com.utesocial.android.feature_notification.presentation.notification.state_holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.utesocial.android.UteSocial
import com.utesocial.android.core.domain.util.Resource
import com.utesocial.android.feature_notification.domain.use_case.NotificationUseCase
import com.utesocial.android.feature_notification.presentation.notify.state_holder.state.NotifyState
import com.utesocial.android.feature_notification.presentation.request.state_holder.state.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotificationViewModel(private val notificationUseCase: NotificationUseCase) : ViewModel() {

    companion object {

        val Factory:ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = (this[APPLICATION_KEY] as UteSocial).appModule
                NotificationViewModel(appModule.notificationUseCase)
            }
        }
    }

    private val _notifyState = MutableStateFlow(NotifyState())
    val notifyState: StateFlow<NotifyState> = _notifyState

    private val _notifyBadge = MutableStateFlow(0)
    val notifyBadge: StateFlow<Int> = _notifyBadge

    private val _requestState = MutableStateFlow(RequestState())
    val requestState: StateFlow<RequestState> = _requestState

    private val _requestBadge = MutableStateFlow(0)
    val requestBadge: StateFlow<Int> = _requestBadge

    init {
        getNotifies()
        getRequests()
    }

    fun getNotifies() = notificationUseCase.getNotifies().onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _notifyBadge.value = 0
                _notifyState.value = NotifyState(isLoading = true)
            }
            is Resource.Success -> {
                val notifies = resource.data ?: emptyList()
                var notifyBadge = 0

                for (notify in notifies) {
                    if (notify.unread) {
                        notifyBadge++
                    }
                }

                _notifyBadge.value = notifyBadge
                _notifyState.value = NotifyState(notifies = notifies)
            }
            is Resource.Error -> {
                _notifyBadge.value = 0
                _notifyState.value = NotifyState(error = resource.message ?: "An unexpected error occurred")
            }
        }
    }.launchIn(viewModelScope)

    fun getRequests() = notificationUseCase.getRequests().onEach { resource ->
        when (resource) {
            is Resource.Loading -> {
                _requestBadge.value = 0
                _requestState.value = RequestState(isLoading = true)
            }
            is Resource.Success -> {
                val requests = resource.data ?: emptyList()
                var requestBadge = 0

                for (request in requests) {
                    if (request.unread) {
                        requestBadge++
                    }
                }

                _requestBadge.value = requestBadge
                _requestState.value = RequestState(requests = requests)
            }
            is Resource.Error -> {
                _requestBadge.value = 0
                _requestState.value = RequestState(error = resource.message ?: "An unexpected error occurred")
            }
        }
    }.launchIn(viewModelScope)
}