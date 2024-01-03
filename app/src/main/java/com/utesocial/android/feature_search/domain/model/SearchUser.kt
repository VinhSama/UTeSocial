package com.utesocial.android.feature_search.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.utesocial.android.core.domain.model.User
import java.io.Serializable

@Entity(indices = [Index(value = ["userId"], unique = true)])
data class SearchUser(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @Embedded(prefix = "details_")
    val user: User,
    @ColumnInfo(name = "friendState")
    val friendState: String
) : Serializable