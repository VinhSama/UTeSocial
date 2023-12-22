package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName

data class Like (
    val id : String?,
    @SerializedName("userType")
    val userType : UserType?,
    val user : UserAuthor?,
    val userPage : PageAuthor?
) {
    enum class UserType {
        @SerializedName("User")
        USER,
        @SerializedName("UserPage")
        USER_PAGE;

        override fun toString(): String {
            return javaClass
                .getField(name)
                .getAnnotation(SerializedName::class.java)
                ?.value ?: ""
        }


    }
}