package com.utesocial.android.feature_community.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.domain.model.Avatar
import com.utesocial.android.core.domain.model.User
import java.io.Serializable
import java.util.Date
@Entity(indices = [Index(value = ["requestId"], unique = true)])
data class FriendRequestEntity(
    @PrimaryKey(autoGenerate = false)
    val requestId: String,
    val userId: String,
    val username: String,
    val fullName: String,
    @Embedded
    val avatar: Avatar?,
    val status: FriendRequest.FriendState,
    val createdAt: Date
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendRequestEntity

        if (requestId != other.requestId) return false
        if (userId != other.userId) return false
        if (username != other.username) return false
        if (fullName != other.fullName) return false
        if (avatar != other.avatar) return false
        if (status != other.status) return false
        return createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = requestId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}


data class FriendRequest(
    @SerializedName("_id")
    val requestId: String,
    val sender: User,
    val status: FriendState,
    val createdAt: Date,
) : Serializable {

    override fun toString(): String {
        return "FriendRequest(requestId='$requestId', sender=$sender, status=$status, createdAt=$createdAt"
    }

    enum class FriendState {
        @SerializedName("Accepted")
        ACCEPTED,
        @SerializedName("Pending")
        PENDING,
        @SerializedName("Rejected")
        REJECTED
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendRequest

        if (requestId != other.requestId) return false
        if (sender != other.sender) return false
        if (status != other.status) return false
        return createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = requestId.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }

    fun toEntity() : FriendRequestEntity {
        return FriendRequestEntity(
            requestId,
            sender.userId,
            sender.username,
            "${sender.firstName} ${sender.lastName}",
            sender.avatar,
            status,
            createdAt
        )
    }
}

