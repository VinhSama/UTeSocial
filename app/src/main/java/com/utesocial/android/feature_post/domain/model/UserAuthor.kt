package com.utesocial.android.feature_post.domain.model

import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.domain.model.Avatar
import java.io.Serializable

data class UserAuthor(
    @SerializedName("_id")
    val id : String = "",
    val username: String = "",
    val fullName: String = "",
    val avatar: String = "",
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAuthor

        if (id != other.id) return false
        if (username != other.username) return false
        if (fullName != other.fullName) return false
        return avatar == other.avatar
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + fullName.hashCode()
        result = 31 * result + avatar.hashCode()
        return result
    }
}