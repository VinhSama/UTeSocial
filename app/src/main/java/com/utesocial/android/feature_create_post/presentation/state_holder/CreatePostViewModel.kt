package com.utesocial.android.feature_create_post.presentation.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(): ViewModel() {

    val disposable = CompositeDisposable()
    val inputSelectorUsing = MutableLiveData<InputSelectorEvent>(InputSelectorEvent.NoneSelector)
    val mediaItems by lazy { MutableLiveData<List<MediaItem>>(emptyList())}
    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }


}