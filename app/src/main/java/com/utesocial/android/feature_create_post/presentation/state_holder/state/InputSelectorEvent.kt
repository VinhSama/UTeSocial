package com.utesocial.android.feature_create_post.presentation.state_holder.state

sealed class InputSelectorEvent {
    data object NoneSelector : InputSelectorEvent()
    data object EmojiSelector : InputSelectorEvent()
    data object MediaSelector : InputSelectorEvent()
    data object LocationSelector : InputSelectorEvent()
    data object TagSelector : InputSelectorEvent()
}