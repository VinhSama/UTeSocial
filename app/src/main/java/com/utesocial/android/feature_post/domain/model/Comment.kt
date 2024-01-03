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
    val userId: String = ""
) : Serializable {

}