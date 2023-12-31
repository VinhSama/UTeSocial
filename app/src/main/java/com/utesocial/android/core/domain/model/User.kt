package com.utesocial.android.core.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.utesocial.android.core.data.util.Common
import io.reactivex.rxjava3.annotations.NonNull
import java.io.Serializable
import java.util.Date

@Entity(indices = [Index(value = ["userId"], unique = true)])
data class User(
    @SerializedName("_id")
    @PrimaryKey(autoGenerate = false)
    var userId: String,
    var identityCode: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var username: String,
    var homeTown: String,
    var birthdate: String,
    @Embedded(prefix = "avatar_")
    var avatar: Avatar? = null,
    var status: Int?,
    var friends: List<String>,
    var friendCount: Int?,
    var type: UserType?,
    @Embedded
    var details: UserDetails?
) : Serializable {

    companion object {
        val EMPTY: User = User()
    }

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        emptyList(),
        null,
        null,
        null,
    )
    @JsonAdapter(UserTypeAdapter::class)
    enum class UserType(val type: Int) {
        UNDEFINED(0),
        CollegeStudent(1),
        Lecturer(2),
        Candidate(3);

        companion object {
            fun fromInt(value: Int): UserType? {
                return entries.find { it.type == value }
            }
        }

    }

    class UserTypeAdapter : TypeAdapter<UserType>() {
        override fun write(out: JsonWriter?, value: UserType?) {
            out?.value(value?.type)
        }

        override fun read(`in`: JsonReader?): UserType? {
            if(`in`?.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return null
            }
            return UserType.fromInt(`in`?.nextInt() ?: 0)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (identityCode != other.identityCode) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (homeTown != other.homeTown) return false
        if (birthdate != other.birthdate) return false
        if (avatar != other.avatar) return false
        if (status != other.status) return false
        if (friends != other.friends) return false
        if (friendCount != other.friendCount) return false
        if (type != other.type) return false
        return details == other.details
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + identityCode.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + homeTown.hashCode()
        result = 31 * result + birthdate.hashCode()
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + friends.hashCode()
        result = 31 * result + (friendCount ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (details?.hashCode() ?: 0)
        return result
    }


}