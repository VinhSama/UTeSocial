package com.utesocial.android.feature_search.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.utesocial.android.core.domain.model.User
import java.io.Serializable
@Entity
data class SearchUser(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @Embedded(prefix = "result_")
    val user: User,
    val friendState: String = "",
    val isSender: Boolean? = false
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchUser

        if (userId != other.userId) return false
        if (user != other.user) return false
        if (friendState != other.friendState) return false
        return isSender == other.isSender
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + friendState.hashCode()
        result = 31 * result + (isSender?.hashCode() ?: 0)
        return result
    }
}