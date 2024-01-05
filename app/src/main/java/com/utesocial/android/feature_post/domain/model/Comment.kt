package com.utesocial.android.feature_post.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date
@Entity(indices = [Index(value = ["commentId"], unique = true)])
data class Comment(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    val commentId: String = "",
    val text: String = "",
    val image: List<PostResource> = emptyList(),
    val createdAt: Date,
    val fullName: String = "",
    val username: String = "",
    val avatar: String = "",
    val userId: String = "",
    val post: String = ""
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (commentId != other.commentId) return false
        if (text != other.text) return false
        if (image != other.image) return false
        if (createdAt != other.createdAt) return false
        if (fullName != other.fullName) return false
        if (username != other.username) return false
        if (avatar != other.avatar) return false
        return userId == other.userId
    }

    override fun hashCode(): Int {
        var result = commentId.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + avatar.hashCode()
        result = 31 * result + userId.hashCode()
        return result
    }
}