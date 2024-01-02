package com.utesocial.android.feature_post.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date
@Entity(indices = [Index(value = ["id"], unique = true)])
data class PostModel(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    val id : String,
    @Embedded(prefix = "userAuthor_")
    val userAuthor: UserAuthor?,
    @Embedded(prefix = "pageAuthor_")
    val userPageAuthor: PageAuthor?,
    val group : String?,
    val content : String?,
    val postResources: List<PostResource> = emptyList(),
    val likeCounts : Int,
    val likes : List<LikesPostHeader> = emptyList(),
    val sharedPost : String?,
    val privacyMode : Int,
    val tags : List<String>,
    val shares : Int,
    val createdAt: Date,
    val updatedAt: Date
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostModel

        if (id != other.id) return false
        if (userAuthor != other.userAuthor) return false
        if (userPageAuthor != other.userPageAuthor) return false
        if (group != other.group) return false
        if (content != other.content) return false
        if (postResources != other.postResources) return false
        if (likeCounts != other.likeCounts) return false
        if (likes != other.likes) return false
        if (sharedPost != other.sharedPost) return false
        if (privacyMode != other.privacyMode) return false
        if (tags != other.tags) return false
        if (shares != other.shares) return false
        if (createdAt != other.createdAt) return false
        return updatedAt == other.updatedAt
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (userAuthor?.hashCode() ?: 0)
        result = 31 * result + (userPageAuthor?.hashCode() ?: 0)
        result = 31 * result + (group?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (postResources?.hashCode() ?: 0)
        result = 31 * result + likeCounts
        result = 31 * result + likes.hashCode()
        result = 31 * result + (sharedPost?.hashCode() ?: 0)
        result = 31 * result + privacyMode
        result = 31 * result + tags.hashCode()
        result = 31 * result + shares
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "PostModel(id='$id', userAuthor=$userAuthor, userPageAuthor=$userPageAuthor, group=$group, content=$content, postResources=$postResources, likeCounts=$likeCounts, likes=$likes, sharedPost=$sharedPost, privacyMode=$privacyMode, tags=$tags, shares=$shares, createdAt=$createdAt, updatedAt=$updatedAt)"
    }


}