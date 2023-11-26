package com.utesocial.android.core.presentation.main.state_holder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class MainViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {

        val Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return MainViewModel(savedStateHandle) as T
            }
        }
    }
}