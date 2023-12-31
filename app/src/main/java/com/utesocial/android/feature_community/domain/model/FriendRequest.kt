package com.utesocial.android.feature_community.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.utesocial.android.core.domain.model.User
import java.io.Serializable
import java.util.Date

@Entity(indices = [Index(value = ["requestId"], unique = true)])
data class FriendRequest(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    val requestId: String,
    @Embedded
    val sender: User,
    @ColumnInfo(name = "friendState")
    val status: FriendState,
    @ColumnInfo(name = "sendAt")
    val createdAt: Date,
) : Serializable {

    enum class FriendState {
        @SerializedName("Accepted")
        ACCEPTED,
        @SerializedName("Pending")
        PENDING,
        @SerializedName("Rejected")
        REJECTED
    }

    override fun toString(): String {
        return "FriendRequest(requestId='$requestId', sender=$sender, status=$status, createdAt=$createdAt"
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


}

