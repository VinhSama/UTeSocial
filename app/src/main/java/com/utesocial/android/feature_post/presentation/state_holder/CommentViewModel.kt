package com.utesocial.android.feature_post.presentation.state_holder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {
    val disposable = CompositeDisposable()
    val inputSelectorUsing = MutableLiveData<InputSelectorEvent>(InputSelectorEvent.NoneSelector)
    val validateUIState = MutableLiveData(false)

    override fun onCleared() {
        if(!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onCleared()
    }
}