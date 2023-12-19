package com.utesocial.android.feature_post.domain.model

data class Like (
    val id : String?,
    val userType : UserType?,
    val user : UserAuthor?,
    val userPage : PageAuthor?
) {
    enum class UserType(val type: String) {
        USER("User"),
        USER_PAGE("UserPage")
    }
}